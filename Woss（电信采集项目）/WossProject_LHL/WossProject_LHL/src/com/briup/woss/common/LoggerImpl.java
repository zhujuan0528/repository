package com.briup.woss.common;

import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggerImpl implements com.briup.util.Logger{
    private String filePath;
    private Logger defaultLogger=Logger.getRootLogger();

    @Override
    public void init(Properties arg0){
        filePath=arg0.getProperty("log-properties");
        PropertyConfigurator.configure(filePath);
    }

    @Override
    public void debug(String arg0){
        defaultLogger.debug(arg0);
    }

    @Override
    public void error(String arg0){
        defaultLogger.error(arg0);
    }

    @Override
    public void fatal(String arg0){
        defaultLogger.fatal(arg0);
    }

    @Override
    public void info(String arg0){
        defaultLogger.info(arg0);
    }

    @Override
    public void warn(String arg0){
        defaultLogger.warn(arg0);
    }
    
    public static void main(String[] args){
        LoggerImpl log=new LoggerImpl();
        log.info("this is info");
        log.warn("this is warn");
        log.error("this is error");
        log.fatal("this is fatal");
        log.debug("this is debug");
    }
}
