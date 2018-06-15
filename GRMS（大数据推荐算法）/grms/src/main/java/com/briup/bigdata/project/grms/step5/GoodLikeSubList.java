package com.briup.bigdata.project.grms.step5;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.Iterator;

public class GoodLikeSubList extends Configured implements Tool {

    static class GoodLikeSubListMapper extends Mapper<LongWritable,Text,Text,Text>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] strings = value.toString().split("\t");

            if(strings[0].contains("2000")){
                context.write(new Text(strings[0]),new Text(strings[1]));
            }else{
                String[] rights = strings[1].split(",");
                for(int i=0;i<rights.length;i++){
                    context.write(new Text(strings[0]+","+rights[i]),new Text("*"));
                }
            }


        }
    }

    static class  GoodLikeSubListReduce extends Reducer<Text,Text,Text,Text>{

        StringBuilder stringBuilder =new StringBuilder();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            Iterator<Text> iterator = values.iterator();
            String value1 = iterator.next().toString();
            if(!iterator.hasNext()){
                String[] results = key.toString().split(",");
                context.write(new Text(results[0]),new Text(results[1]));
            }
        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        /*Path input = new Path(conf.get("in"));
        Path output = new Path(conf.get("out"));*/
        Path input=new Path(strings[0]);
        Path input1=new Path(strings[1]);
        Path output=new Path(strings[2]);
        Job job = Job.getInstance(conf,this.getClass().getSimpleName());
        job.setJarByClass(this.getClass());

        job.setMapperClass(GoodLikeSubListMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,input);
        TextInputFormat.addInputPath(job,input1);


        job.setReducerClass(GoodLikeSubListReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,output);
        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String args[]) throws Exception {
        String[] strings = new String[3];
        strings[0] = "/user/output/step1/part-r-00000";
        strings[1] = "/user/output/step6/part-r-00000";
        strings[2] = "/user/output/step7";
        System.exit(ToolRunner.run(new GoodLikeSubList(),strings));
    }
}
