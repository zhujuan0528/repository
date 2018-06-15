package com.briup.woss.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import com.briup.util.BackUP;

public class BackupImpl implements BackUP{
    // private String fileName = "backup";
    // private String fileName = "src/com/briup/radwtmp";
    private String fileName;

    @Override
    public void init(Properties arg0){
        System.out.println(fileName);
        fileName=arg0.getProperty("back-temp");
    }

    @Override
    public Object load(String key,boolean flag) throws Exception{
        File file=new File(fileName,key);
        if(!file.exists()){ return null; }
        FileInputStream fis=new FileInputStream(file);
        ObjectInputStream ois=new ObjectInputStream(fis);
        Object o=ois.readObject();
        ois.close();
        fis.close();
        if(flag==BackUP.LOAD_REMOVE){
            file.delete();
        }
        return o;
    }

    @Override
    public void store(String key,Object obj,boolean flag) throws Exception{
        File file=new File(fileName,key);
        System.out.println(file.getName());
        if(!file.exists()){
            file.createNewFile();
        }
        FileOutputStream fos=new FileOutputStream(file,flag);// 如果flag为true的话，则将字节写到文件末尾
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        fos.close();
    }
    
    public static void main(String[] args){
        BackupImpl back=new BackupImpl();
        try{
            back.store("lina","my name is tom",BackUP.STORE_OVERRIDE);
            back.store("lina","hello",BackUP.STORE_APPEND);
            Object o=back.load("lina",BackUP.LOAD_UNREMOVE);
            System.out.println(o.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
