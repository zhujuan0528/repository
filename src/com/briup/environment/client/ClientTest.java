package com.briup.environment.client;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.ConfigurationImpl;

import java.util.Collection;

public class ClientTest {
    public static void main(String[] args) throws Exception {
        ConfigurationImpl configuration = new ConfigurationImpl();
        Collection<Environment> coll = configuration.getGather().gather();
        configuration.getClient().send(coll);
    }
}
