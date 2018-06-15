package com.briup.environment.client;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.EMDCModule;

import java.util.Collection;

/*
* Gather接口规定了采集模块所应用的方法
* 开始对物联网数据中心项目环境信息进行
* 采集，将采集的数据封装成Collectioni<Environment>集合
* */
public interface Gather extends EMDCModule{
    Collection<Environment> gather()throws Exception;


}
