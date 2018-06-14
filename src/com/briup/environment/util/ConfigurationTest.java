package com.briup.environment.util;

public class ConfigurationTest {
    public static void main(String[] args) throws Exception {
        ConfigurationImpl configuration = new ConfigurationImpl();
        System.out.println(configuration.getClient());
        System.out.println(configuration.getGather());
        System.out.println(configuration.getServer());
        System.out.println(configuration.getDBStore());
        System.out.println(configuration.getLogger());
        configuration.getLogger().debug("sdfdsf");
        System.out.println(configuration.getBackUp());

    }
}
