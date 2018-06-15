package com.briup.bigdata.project.grms.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class JobUtil{
    public static Job xyz;
    public static String in;
    public static String out;

    public static void setConf(
            Configuration c,    // MapReduce作业传递的整个作业的配置对象
            Class cz,           // MapReduce作业志行的jar包中包含的主类的镜像
            String name,        // 作业的名字
            String vin,         // 数据的输入路径
            String vout         // 数据的输出路径
    ){
        try{
            xyz=Job.getInstance(c,name);        // 构建Job对象，设置配置对象和作业名
            xyz.setJarByClass(cz);              // 提供执行的作业的主类的镜像
            in=vin;                             // 将数据的输入路径传递给全局变量
            out=vout;                           // 将数据的输出路径传递给全局变变量
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void setMapper(
        Class<? extends Mapper> x,                  // 设置作业中运行的Mapper类的镜像参数
        Class<? extends Writable> y,      // 设置作业中Mapper的Key的数据类型参数
        Class<? extends Writable> z,                // 设置作业中Mapper的Value的数据类型参数
        Class<? extends InputFormat> o              // 设置作业中数据输入的格式参数
    ){
        try{
            xyz.setMapperClass(x);
            xyz.setMapOutputKeyClass(y);
            xyz.setMapOutputValueClass(z);
            xyz.setInputFormatClass(o);
            o.getMethod("addInputPath",Job.class,Path.class).invoke(null,xyz,new Path(in));
        }catch( NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }
    }

    public static void setReducer(
            Class<? extends Reducer> a,                 // 设置作业中运行的Reducer类的镜像参数
            Class<? extends Writable> b,      // 设置作业中Reducer的Key的数据类型参数
            Class<? extends Writable> c,      // 设置作业中Reducer的Value的数据类型参数
            Class<? extends OutputFormat> d){           // 设置作业中数据输出的格式参数
        try{
            xyz.setReducerClass(a);
            xyz.setOutputKeyClass(b);
            xyz.setOutputValueClass(c);
            xyz.setOutputFormatClass(d);
            d.getMethod("setOutputPath",Job.class,Path.class).invoke(null,xyz,new Path(out));
        }catch(IllegalAccessException | InvocationTargetException | NoSuchMethodException e){
            e.printStackTrace();
        }
    }

    public static void setCombiner(boolean flag,Class<? extends Reducer> combiner){
        if(flag&&combiner!=null){
            xyz.setCombinerClass(combiner);
        }
    }

    public static int commit() throws InterruptedException, IOException, ClassNotFoundException{
        return xyz.waitForCompletion(true)?0:1;         // 提交作业
    }
}
