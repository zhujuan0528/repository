package com.briup.bigdata.project.grms.step1;

import com.briup.bigdata.project.grms.util.JobUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
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

public class UserBuyGoodList extends Configured implements Tool {



    static class UserBuyGoodListMapper extends Mapper<LongWritable,Text,Text,Text>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] strings = value.toString().split("\t");
            context.write(new Text(strings[0]),new Text(strings[1]));
        }
    }

    static class UserBuyGoodListReduce extends Reducer<Text,Text,Text,Text>{

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            Iterator<Text> iterator = values.iterator();
            StringBuilder builder = new StringBuilder();
            while (iterator.hasNext()){
                builder.append(iterator.next().toString()+",");
            }
            String result =  builder.substring(0,builder.length()-1);
            context.write(key,new Text(result));
        }
    }

    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        /*Path input = new Path(conf.get("in"));
        Path output = new Path(conf.get("out"));*/
        Path input=new Path(strings[0]);
        Path output=new Path(strings[1]);
        Job job = Job.getInstance(conf,this.getClass().getSimpleName());
        job.setJarByClass(this.getClass());

        job.setMapperClass(UserBuyGoodListMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,input);

        job.setReducerClass(UserBuyGoodListReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,output);
        return job.waitForCompletion(true)?0:1;

    }

    public static void main(String[] args) throws Exception {

        String[] strings = new String[2];
        strings[0] = "/user/input/matrix.txt";
        strings[1] = "/user/output/step1";
        System.exit(ToolRunner.run(new UserBuyGoodList(),strings));
    }
}
