package com.briup.woss.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Properties;
import com.briup.util.BIDR;
import com.briup.util.BackUP;
import com.briup.util.Configuration;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.common.BackupImpl;
import com.briup.woss.common.LoggerImpl;

public class DBStoreImpl implements DBStore,ConfigurationAWare{
	/*
	 * private String url = "jdbc:oracle:thin:@localhost:1521:XE";
	 * private String driver = "oracle.jdbc.driver.OracleDriver";
	 * private String userName = "woss_gather";
	 * private String passWord = "woss_gather";
	 * private String batchSize = "50";
	 * private LoggerImpl log = new LoggerImpl();
	 * private BackupImpl back = new BackupImpl();
	 */
	private String url;
	private String driver;
	private String userName;
	private String passWord;
	private String batchSize;
	private LoggerImpl log;
	private BackupImpl back;
	private Configuration configuration;

	@Override
	public void init(Properties properties){
		url=properties.getProperty("url");
		driver=properties.getProperty("driver");
		userName=properties.getProperty("userName");
		passWord=properties.getProperty("passWord");
		batchSize=properties.getProperty("batch-size");
	}

	@Override
	public void setConfiguration(Configuration arg0){
		this.configuration=arg0;
		try{
			log=(LoggerImpl)configuration.getLogger();
			back=(BackupImpl)configuration.getBackup();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@SuppressWarnings({"deprecation","unchecked"})
	@Override
	public void saveToDB(Collection<BIDR> arg0) throws Exception{
		Connection conn=null;
		PreparedStatement ps=null;
		try{
			Class.forName(driver);
			conn=DriverManager.getConnection(url,userName,passWord);
			conn.setAutoCommit(false);
			log.info("等待正在存入数据库");
			// 加载备份
			Collection<BIDR> c=(Collection<BIDR>)back.load("dbstoreBack",BackUP.LOAD_REMOVE);
			if(c!=null){
				log.info("正在装载备份数据");
				arg0.addAll(c);
				log.info("备份数据装载完成");
			}
			int i=0;
			int count=0;
			for(BIDR b:arg0){
				int day=b.getLogin_date().getDate();
				if(day!=i){
					if(ps!=null){
						ps.executeBatch();
						ps.close();
					}
					i=day;
					String sql="insert into t_detail_"+day+" values(?,?,?,?,?,?)";
					ps=conn.prepareStatement(sql);
				}
				ps.setString(1,b.getAAA_login_name());
				ps.setString(2,b.getLogin_ip());
				ps.setTimestamp(3,b.getLogin_date());
				ps.setTimestamp(4,b.getLogout_date());
				ps.setString(5,b.getNAS_ip());
				ps.setInt(6,b.getTime_deration());
				ps.addBatch();
				count++;
				if(((count%(Integer.parseInt(batchSize)))==0)||(count==arg0.size())){
					ps.executeBatch();
					conn.commit();
				}
			}
			log.info("存储完毕");
			log.info("存储数据行数："+arg0.size());
		}catch(Exception e){
			// 备份
			log.error("发生错误，数据回滚");
			back.store("dbstoreBack",arg0,BackUP.STORE_OVERRIDE);
			if(conn!=null){
				conn.rollback();
			}
		}finally{
			if(ps!=null){
				ps.close();
			}
		}
	}
}
