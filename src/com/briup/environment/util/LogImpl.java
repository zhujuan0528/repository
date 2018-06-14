package com.briup.environment.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;

public class LogImpl implements Log,ConfigurationAware {
    private String filePath;
    private Configuration configuration;
    private Logger log = Logger.getRootLogger();
    public LogImpl() {

        //PropertyConfigurator.configure(filePath);
    }
    @Override
    public void debug(String msg) {
        log.debug(msg);
    }

    @Override
    public void info(String msg) {
        log.info(msg);
    }

    @Override
    public void warn(String msg) {
        log.warn(msg);
    }

    @Override
    public void error(String msg) {
        log.error(msg);
    }

    @Override
    public void fatal(String msg) {
        log.fatal(msg);
    }


    @Override
    public void init(Properties properties) throws Exception {
        filePath=properties.getProperty("log-file");
        PropertyConfigurator.configure(filePath);
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration=configuration;


    }

}
