package com.briup.environment.server;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.EMDCModule;

import java.util.Collection;

public interface Server extends EMDCModule{
    Collection<Environment> reciver()throws Exception;
    void shutdown();

}
