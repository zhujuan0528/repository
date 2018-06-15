package com.briup.woss.server;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;
import com.briup.util.BIDR;
import com.briup.util.BackUP;
import com.briup.util.Configuration;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.common.BackupImpl;
import com.briup.woss.common.LoggerImpl;

public class ServerImpl implements Server,ConfigurationAWare{
    /*
     * private int port = 9000;
     * private DBStoreImpl dbstore = new DBStoreImpl();
     * private LoggerImpl log = new LoggerImpl();
     * private BackupImpl back = new BackupImpl();
     */
    private int port;
    private DBStore dbstore;
    private LoggerImpl log;
    private BackupImpl back;
    private Configuration configuration;

    @Override
    public void init(Properties arg0){
        port=Integer.parseInt(arg0.getProperty("port"));
    }

    @Override
    public void setConfiguration(Configuration arg0){
        this.configuration=arg0;
        try{
            dbstore=configuration.getDBStore();
            log=(LoggerImpl)configuration.getLogger();
            back=(BackupImpl)configuration.getBackup();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<BIDR> revicer() throws Exception{
        log.info("服务器已运行，等待客户端连接");
        ServerSocket ss=new ServerSocket(port);
        Socket socket=ss.accept();
        log.info("客户端连接成功，接收数据");
        ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
        Collection<BIDR> coll=(Collection<BIDR>)ois.readObject();
        log.info("接受数据的行数："+coll.size());
        log.info("准备将数据存入数据库");
        // 加载备份
        Collection<BIDR> c=(Collection<BIDR>)back.load("serverBack",BackUP.LOAD_REMOVE);
        if(c!=null){
            log.warn("服务器正在加载备份数据");
            coll.addAll(c);
            log.warn("备份数据加载完成");
        }
        try{
            dbstore.saveToDB(coll);
        }catch(Exception e){
            // 备份
            log.error("发生错误，服务器正在备份数据");
            try{
                back.store("serverBack",coll,BackUP.STORE_OVERRIDE);
                log.warn("数据备份成功");
            }catch(Exception e2){
                log.error("数据备份失败");
            }
        }
        ois.close();
        socket.close();
        ss.close();
        return coll;
    }

    @Override
    public void shutdown(){
    }
}
