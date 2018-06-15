package com.briup.bigdata.project.grms.step5;

import com.briup.bigdata.project.grms.step4.UserGoodsList;
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

public class GoodsLikeList extends Configured implements Tool {

    static class GoodsLikeListMapper extends Mapper<LongWritable,Text,Text,Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] strings =  value.toString().split("\t");
            context.write(new Text(strings[0]),new Text(strings[1]));
        }
    }

    static class GoodsLikeListReduce extends Reducer<Text,Text,Text,Text>{
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            Iterator<Text> iterator = values.iterator();
            String[] str1 =  iterator.next().toString().split(",");
            String[] str2 =  iterator.next().toString().split(",");
            String[] swap;

            if(str1[0].contains("1000")){
                swap = str1;
                str1 = str2;
                str2 = swap;
            }

            for(int i=0;i<str2.length;i++){
                String left = str2[i].substring(0,str2[i].indexOf(":"));
                for(int j=0;j<str1.length;j++){
                    String[] rights = str1[j].split(":");
                    context.write(new Text(left+","+rights[0]),new Text(rights[1]));
                }
            }
        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        /*Path input = new Path(conf.get("in"));
        Path output = new Path(conf.get("out"));*/
        Path input=new Path(strings[0]);
        Path input1 =new Path(strings[1]);
        Path output=new Path(strings[2]);
        Job job = Job.getInstance(conf,this.getClass().getSimpleName());
        job.setJarByClass(this.getClass());

        job.setMapperClass(GoodsLikeListMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,input);
        TextInputFormat.addInputPath(job,input1);

        job.setReducerClass(GoodsLikeListReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,output);
        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String args[]) throws Exception {
        String[] strings = new String[3];
        strings[0] = "/user/output/step3/part-r-00000";
        strings[1] = "/user/output/step4/part-r-00000";
        strings[2] = "/user/output/step5";
        System.exit(ToolRunner.run(new GoodsLikeList(),strings));
    }
}
