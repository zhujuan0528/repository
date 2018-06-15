package com.briup.woss.main;

import java.util.Collection;
import com.briup.util.BIDR;
import com.briup.woss.client.ClientImpl;
import com.briup.woss.client.GatherImpl;
import com.briup.woss.common.ConfigurationImpl;

public class Client{
    
    public static void main(String[] args){
        /*
         * ClientImpl client = new ClientImpl();
         * GatherImpl gather = new GatherImpl();
         */
        ConfigurationImpl conf=new ConfigurationImpl();
        try{
            GatherImpl gather=(GatherImpl)conf.getGather();
            ClientImpl client=(ClientImpl)conf.getClient();
            Collection<BIDR> c=gather.gather();
            client.send(c);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
