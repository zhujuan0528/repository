package com.briup.environment.server;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Properties;

public class DBStoreImpl implements DBStore,ConfigurationAware {
    private String driver;
    private String url;
    private String userName;
    private String passWord;
    private Connection connection;
    private PreparedStatement ps;
    private int batchSize;
    private LogImpl log;
    private BackUpImpl back;
    private Configuration configuration;
    @Override
    public void saveDB(Collection<Environment> c) throws Exception {
        Class.forName(driver);
        connection = DriverManager.getConnection(url, userName, passWord);
        int count = 0;
        int day = 0;
        log.info("开始入库");
        for (Environment environment : c) {
            /*
             * 在两种情况下需要创建新的ps对象
             *
             *
             * environment.getGather_date()返回的是Timestamp类型
             * Timestamp对象的getDate()方法返回day of month
             * Timestamp对象的getDay()方法返回0-6对应的星期天-星期六
             * */
            if (ps == null || day != environment.getGather_data().getDate()) {
                day = environment.getGather_data().getDate();
                log.debug("数据入库的天数： " + day);
                //System.out.println("数据入库的天数： " + day);
                //日期发生变化，为防止之前的一天内存在没有处理的数据（可能大小不到batchsize），需要手动提交一次数据
                if (ps != null) {
                    ps.executeBatch();//前一日期留下的sql语句
                    ps.clearBatch();
                    ps.close();
                }
                String sql = "INSERT INTO E_DETAIL_" + day + " VALUES (?,?,?,?,?,?,?,?,?)";
                ps = connection.prepareStatement(sql);
            }
            ps.setString(1, environment.getName());
            ps.setString(2, environment.getSrcId());
            ps.setString(3, environment.getDesId());
            ps.setString(4, environment.getSersorAddress());
            ps.setInt(5, environment.getCount());
            ps.setString(6, environment.getCmd());
            ps.setInt(7, environment.getStatus());
            ps.setFloat(8, environment.getData());
            ps.setTimestamp(9, environment.getGather_data());
            //将sql语句放入到批处理中
            ps.addBatch();
            count++;
            if (count % batchSize == 0) {
                ps.executeBatch();
                ps.clearBatch();
            }
            ps.executeBatch();
        }

        if(ps!=null){
            ps.executeBatch();
            ps.clearBatch();
            ps.close();

        }
        log.info("入库完成");
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration=configuration;
        try{
            log=(LogImpl) configuration.getLogger();
            back=(BackUpImpl)configuration.getBackUp();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void init(Properties properties) throws Exception {
        url=properties.getProperty("url");
        driver=properties.getProperty("driver");
        userName=properties.getProperty("userName");
        passWord=properties.getProperty("passWord");
        batchSize=Integer.parseInt(properties.getProperty("batch-size"));
    }
}
