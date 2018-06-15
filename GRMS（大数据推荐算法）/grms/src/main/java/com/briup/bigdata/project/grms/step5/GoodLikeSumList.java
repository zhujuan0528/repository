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

public class GoodLikeSumList extends Configured implements Tool {

    static class GoodLikeSumListMapper extends Mapper<LongWritable,Text,Text,IntWritable>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] strings = value.toString().split("\t");

            context.write(new Text(strings[0]),new IntWritable(Integer.parseInt(strings[1])));
        }
    }

    static class GoodLikeSumListReduce extends Reducer<Text,IntWritable,Text,IntWritable>{
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

            int sum = 0;
            Iterator<IntWritable> iterator = values.iterator();
            while (iterator.hasNext()){
                sum+=iterator.next().get();
            }
            context.write(key,new IntWritable(sum));

        }
    }


    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        /*Path input = new Path(conf.get("in"));
        Path output = new Path(conf.get("out"));*/
        Path input=new Path(strings[0]);
        Path output=new Path(strings[1]);
        Job job = Job.getInstance(conf,this.getClass().getSimpleName());
        job.setJarByClass(this.getClass());

        job.setMapperClass(GoodLikeSumListMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,input);

        job.setReducerClass(GoodLikeSumListReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,output);
        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String args[]) throws Exception {
        String[] strings = new String[2];
        strings[0] = "/user/output/step5/part-r-00000";
        strings[1] = "/user/output/step6";
        System.exit(ToolRunner.run(new GoodLikeSumList(),strings));
    }
}
