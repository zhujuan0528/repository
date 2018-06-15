package com.briup.woss.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import com.briup.util.BIDR;
import com.briup.util.BackUP;
import com.briup.util.Configuration;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.common.BackupImpl;
import com.briup.woss.common.ConfigurationImpl;
import com.briup.woss.common.LoggerImpl;

public class GatherImpl implements Gather,ConfigurationAWare{
	/*
	 * private String path = "src/com/briup/radwtmp/radwtmp";
	 * private String nasIp = "127.0.0.1";
	 * private LoggerImpl log= new LoggerImpl();
	 * private BackupImpl back = new BackupImpl();
	 */
	private String path;
	private String nasIp;
	private LoggerImpl log;
	private BackupImpl back;
	private Configuration configuration;

	@Override
	public void init(Properties arg0){
		nasIp=arg0.getProperty("nas-ip");
		path=arg0.getProperty("src-file");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<BIDR> gather() throws Exception{
		Map<String,BIDR> map=new HashMap<String,BIDR>();
		Collection<BIDR> bidrList=new ArrayList<BIDR>();
		File file=new File(path);
		FileReader fr=new FileReader(file);
		BufferedReader br=new BufferedReader(fr);
		// 加载备份
		long ss=0L;
		Long skip=(Long)back.load("skip",BackUP.LOAD_REMOVE);
		if(skip!=null){
			log.warn("skip some bytes");
			br.skip(skip);
			ss=skip;
		}
		Map<String,BIDR> map1=(Map<String,BIDR>)back.load("mapFile",BackUP.LOAD_REMOVE);
		if(map1!=null){
			log.warn("还原备份数据");
			map.putAll(map1);
		}
		long byskip=0L;
		@SuppressWarnings("unused")
		int count=0;
		String s=null;
		log.info("读取数据");
		while((s=br.readLine())!=null){
			String[] line=s.split("[|]");
			byskip=s.getBytes().length+1;
			ss+=byskip;
			if(line[2].equals("7")){
				BIDR bidr=new BIDR();
				bidr.setAAA_login_name(line[0].substring(1));
				bidr.setLogin_ip(line[4]);
				Timestamp login_time=new Timestamp(Long.parseLong(line[3])*1000);
				bidr.setLogin_date(login_time);
				bidr.setNAS_ip(nasIp);
				map.put(line[4],bidr);
				count++;
			}else{
				BIDR bidr1=map.get(line[4]);
				map.remove(line[4]);
				Timestamp logout_time=new Timestamp(Long.parseLong(line[3])*1000);
				bidr1.setLogout_date(logout_time);
				int time_deration=(int)(logout_time.getTime()-bidr1.getLogin_date().getTime())/1000/60;
				bidr1.setTime_deration(time_deration);
				bidrList.add(bidr1);
			}
		}
		br.close();
		fr.close();
		log.info("收集数据的行数"+bidrList.size());
		// 备份
		log.warn("备份数据");
		back.store("mapFile",map,BackUP.STORE_OVERRIDE);
		back.store("skip",ss,BackUP.STORE_OVERRIDE);
		return bidrList;
	}

	public static void main(String[] args) throws Exception{
		// GatherImpl gather = new GatherImpl();
		ConfigurationImpl conf=new ConfigurationImpl();
		Gather gather=conf.getGather();
		try{
			Collection<BIDR> c=gather.gather();
			Iterator<BIDR> iter=c.iterator();
			while(iter.hasNext()){
				BIDR b=iter.next();
				System.out.println("login_name:"+b.getAAA_login_name()+",login_ip:"+b.getLogin_ip()+",login_time:"+b.getLogin_date()+",logout_time:"+b.getLogout_date()+",time:"+b.getTime_deration()+",nas_ip:"+b.getNAS_ip());
			}
			System.out.println("采集数目大小："+c.size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void setConfiguration(Configuration config){
		this.configuration=config;
		try{
			log=(LoggerImpl)configuration.getLogger();
			back=(BackupImpl)configuration.getBackup();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
