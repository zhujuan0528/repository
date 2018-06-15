package com.briup.woss.main;

import com.briup.woss.common.ConfigurationImpl;
import com.briup.woss.server.ServerImpl;

public class Server{
    public static void main(String[] args){
        ConfigurationImpl conf=new ConfigurationImpl();
        try{
            ServerImpl server=(ServerImpl)conf.getServer();
            server.revicer();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
