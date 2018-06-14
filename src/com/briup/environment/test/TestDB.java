package com.briup.environment.test;

import com.briup.environment.bean.Environment;
import com.briup.environment.client.GatherImpl;
import com.briup.environment.server.DBStoreImpl;

import java.util.Collection;

public class TestDB {
    public static void main(String[] args) throws Exception {
        GatherImpl gather = new GatherImpl();
        Collection<Environment> gather1 = gather.gather();
        DBStoreImpl dbStore = new DBStoreImpl();
        dbStore.saveDB(gather1);
    }
}
