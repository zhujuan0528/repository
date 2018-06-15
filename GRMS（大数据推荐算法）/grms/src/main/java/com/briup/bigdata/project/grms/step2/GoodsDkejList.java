package com.briup.bigdata.project.grms.step2;

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

public class GoodsDkejList extends Configured implements Tool {


    static class GoodsDkejListMapper extends Mapper<LongWritable,Text,Text,IntWritable>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] strings = value.toString().split("\t")[1].split(",");
            for(int i=0;i<strings.length;i++){
                for(int j=0;j<strings.length;j++){
                    context.write(new Text(strings[i]+"\t"+strings[j]),new IntWritable(1));
                }
            }
        }
    }

    static class GoodsDkejListReduce extends Reducer<Text,IntWritable,Text,IntWritable>{
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            Iterator<IntWritable> iterator = values.iterator();
            int sum = 0;
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

        job.setMapperClass(GoodsDkejListMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,input);

        job.setReducerClass(GoodsDkejListReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,output);
        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String args[]) throws Exception {
        String[] strings = new String[2];
        strings[0] = "/user/output/step1/part-r-00000";
        strings[1] = "/user/output/step2";
        System.exit(ToolRunner.run(new GoodsDkejList(),strings));
    }
}
