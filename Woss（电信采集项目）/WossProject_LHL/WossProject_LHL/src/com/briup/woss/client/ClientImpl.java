package com.briup.woss.client;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;
import com.briup.util.BIDR;
import com.briup.util.BackUP;
import com.briup.util.Configuration;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.common.BackupImpl;
import com.briup.woss.common.LoggerImpl;

public class ClientImpl implements Client,ConfigurationAWare{
	/*
	 * private String ip = "127.0.0.1";
	 * private int port = 9000;
	 * private LoggerImpl log = new LoggerImpl();
	 * private BackupImpl back = new BackupImpl();
	 */
	private String ip;
	private int port;
	private LoggerImpl log;
	private BackupImpl back;

	@Override
	public void init(Properties properties){
		ip=properties.getProperty("ip");
		port=Integer.parseInt(properties.getProperty("port"));
	}

	@Override
	public void setConfiguration(Configuration configuration){
		try{
			log=(LoggerImpl)configuration.getLogger();
			back=(BackupImpl)configuration.getBackup();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void send(Collection<BIDR> bidrs) throws Exception{
		try{
			// 加载备份
			@SuppressWarnings("unchecked")
			Collection<BIDR> list=(Collection<BIDR>)back.load("clientBack",BackUP.LOAD_REMOVE);
			if(list!=null){
				log.warn("找到备份");
				bidrs.addAll(list);
				log.warn("装载备份");
			}
			log.info("正在连接服务器");
			Socket socket=new Socket(ip,port);
			log.info("成功连接服务器，发送数据");
			ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
			log.info("数据发送中");
			oos.writeObject(bidrs);
			oos.flush();
			log.info("发送数据的行数"+bidrs.size());
			log.info("数据发送成功");
			oos.close();
			socket.close();
		}catch(Exception e){
			// 备份
			log.error("正在备份数据");
			try{
				back.store("clientBack",bidrs,BackUP.STORE_OVERRIDE);
				log.warn("back ended!");
			}catch(Exception e2){
				log.error("客户端备份数据错误");
			}
		}
	}
}
