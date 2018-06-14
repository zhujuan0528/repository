package com.briup.environment.util;

import java.io.*;
import java.util.Properties;

public class BackUpImpl implements BackUp {

    private String fileName;
    @Override
    public void store(String filePath, Object obj, boolean append) throws Exception {
        File file;
        LogImpl log = new LogImpl();
        if (append) {
            file = new File(filePath, "a");//a是追加
        } else {
            file = new File(filePath, "w");//w是覆盖
        }
        log.info("存入" + file.getName());
        //System.out.println("存入"+file.getName());
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file, append);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();
    }

    @Override
    public Object load(String filePath, boolean del) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        } else {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object o = objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            if (del) {
                file.delete();
            }
            return o;
        }

    }

    @Override
    public void init(Properties properties) throws Exception {
        //System.out.println(fileName);
        fileName=properties.getProperty("backup-file");
    }
}
