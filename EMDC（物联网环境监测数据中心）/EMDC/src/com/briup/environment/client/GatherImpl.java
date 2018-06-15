package com.briup.environment.client;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.*;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class GatherImpl implements Gather,ConfigurationAware {
    //Environment对象集合用来保持获取的对象数据
    Collection<Environment> coll = new ArrayList<>();
    //采集的原始文件

    private String src;
    //保存上一次读取字节数的文件
    private String record;
    private Log log;
    private BackUpImpl back;
    private Configuration configuration;
    @Override
    public Collection<Environment> gather() throws Exception {
        /*
        * 1.从path2制定的路径读取上一次读取保存到的字节数的文件，第一次该文件不存在，需要分情况判断
        * 2.读取radwtmp文件的有效字节数，将返回的值保存到path2指定的文件中
        * 3.略过上一次读取的字节数，在进行本次解析
        *
        * */
        File file = new File(record);
        long num = 0;//读取字节数
        if(file.exists()){
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
            num = dataInputStream.readLong();

        }
        //randomAccessFile流实现了略过功能，RandomAccessFile提供两个参数 一个表示文件路径一个表示以什么形式读取，r表示只读
        RandomAccessFile randomAccessFile = new RandomAccessFile(src,"r");
        long num2=randomAccessFile.length();
        randomAccessFile.seek(num);
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(record));
        dataOutputStream.writeLong(num2);

        /*
        * 1.构建缓存字符流按行读取数据(readLine)
        * 2.根据|分隔字符串，根据第四个字段的不同分别赋予 温度、湿度、二氧化碳、光照强度的环境名称
        * 3.第七个字段表示16进制环境值，将16进制转换成10进制
        *   如果是温度和湿度数据，前两个字节是温度数据，中间两个字节是试读数据
        *   如果是二氧化碳和光照强度前两个字节解释对应的数据
        * 4.温度和湿度是同一条记录，读取一行需要创建两个Environment对象，并且分别赋予数值
        * 5.封装好的对象添加到coll集合中
        * */
        String s = null;
        String[] str = null;
        Environment environment = null;
        //统计对象个数
        int count = 0;//统计温度和湿度条数(str[3] 是 16)
        int count2 = 0;//光照 str[3]是256
        int count3 = 0;//二氧化碳 str[3]是1280

        while((s=randomAccessFile.readLine())!=null) {
            environment = new Environment();
            str = s.split("[|]");
            environment.setSrcId(str[0]);
            environment.setDesId(str[1]);
            environment.setDevId(str[2]);
            environment.setSersorAddress(str[3]);
            environment.setCount(Integer.parseInt(str[4]));
            environment.setCmd(str[5]);
            environment.setStatus(Integer.parseInt(str[7]));
            Long aLong = new Long(str[8]);
            Timestamp timestamp = new Timestamp(aLong);
            environment.setGather_data(timestamp);
            switch (str[3]) {
                case "16":
                    count++;
                    environment.setName("温度");
                    float value1 = (float)((Integer.parseInt(str[6].substring(0,4),16)*0.00268127)-46.85);
                    environment.setData(value1);
                    coll.add(environment);
                    environment = new Environment();
                    environment.setSrcId(str[0]);
                    environment.setDesId(str[1]);
                    environment.setDevId(str[2]);
                    environment.setSersorAddress(str[3]);
                    environment.setCount(Integer.parseInt(str[4]));
                    environment.setCmd(str[5]);
                    environment.setStatus(Integer.parseInt(str[7]));
                    environment.setGather_data(timestamp);
                    environment.setName("湿度");
                    float value2 = (float)((Integer.parseInt(str[6].substring(4,8),16)*0.00190735)-6);
                    environment.setData(value2);
                    coll.add(environment);
                    break;
                case "256":
                    count2++;
                    environment.setName("光照强度");
                    environment.setData((float) Integer.parseInt(str[6].substring(0,4), 16));
                    coll.add(environment);
                    break;
                case "1280":
                    count3++;
                    environment.setName("CO2");
                    environment.setData((float) Integer.parseInt(str[6].substring(0,4), 16));
                    coll.add(environment);
                    break;
            }
        }
        log.debug("采集环境数据："+coll.size());
        log.info("温度 湿度："+count);
        log.info("光照强度："+count2);
        log.info("二氧化碳："+count3);

        /*
        System.out.println("采集环境数据："+coll.size());
        System.out.println("温度 湿度："+count);
        System.out.println("光照强度："+count2);
        System.out.println("二氧化碳："+count3);*/
        return coll;
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration=configuration;
        try{
            log=configuration.getLogger();
            back=(BackUpImpl)configuration.getBackUp();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void init(Properties properties) throws Exception {
        src=properties.getProperty("src-file");
        record=properties.getProperty("record-file");
    }


}
