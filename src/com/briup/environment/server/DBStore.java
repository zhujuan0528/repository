package com.briup.environment.server;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.EMDCModule;

import java.util.Collection;
/*
DBStore提供了入库模块的规范，该接口实现类将Environment集合进行持久化
 */
public interface DBStore extends EMDCModule {
    void saveDB(Collection<Environment> c)throws Exception;
}
