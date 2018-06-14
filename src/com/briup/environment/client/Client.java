package com.briup.environment.client;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.EMDCModule;

import java.util.Collection;

public interface Client extends EMDCModule{
    void send(Collection<Environment> collection)throws Exception;
}
