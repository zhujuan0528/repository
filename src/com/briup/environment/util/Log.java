package com.briup.environment.util;

public interface Log extends EMDCModule{
    /*
        * 该接口提供了日志模块的规范，日志模块将日志信息
        * 划分为五种级别，不同级别的日志的记录格式，记录方式不尽相同
    */

    void debug(String msg);//记录debug级别的日志
    void info(String msg);//记录info级别的日志
    void warn(String msg);//记录warn级别的日志
    void error(String msg);//记录error级别的日志
    void fatal(String msg);//记录fatal级别的日志

}
