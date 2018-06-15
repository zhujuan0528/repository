package com.briup.environment.client;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.*;

import java.io.*;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

public class ClientImpl implements Client,ConfigurationAware {
    private String ip;
    private int port;
    private Socket socket = null;
    private OutputStream os = null;
    private ObjectOutputStream oos = null;
    private LogImpl log;
    private BackUpImpl back;
    private Configuration configuration;
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
    public void init(Properties properties) {
        ip=properties.getProperty("ip");
        port=Integer.parseInt(properties.getProperty("port"));

    }

    @Override
    public void send(Collection<Environment> collection) {
        log.info("Connecting...");
        //System.out.println("Connecting...");
        try {
            socket = new Socket(ip, port);
            log.info("Connected! Now sending...");
            //System.out.println("Connected! Now sending...");
            os = socket.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            oos = new ObjectOutputStream(bos);
            oos.writeObject(collection);
            log.info("Sent complete!");
            //System.out.println("Sent complete!");
            oos.flush();
        }catch (Exception e){
            e.printStackTrace();
            log.error("send fail");
        }
    }



}
