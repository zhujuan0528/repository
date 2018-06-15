import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;

import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import org.apache.hadoop.util.*;

import java.io.IOException;
import java.util.Iterator;

public class UserBuyGoodsList extends Configured implements Tool{
    static class UserBuyGoodsListMapper extends Mapper<LongWritable,Text,Text,Text>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split("\t");
            context.write(new Text(split[0]),new Text(split[1]));
        }
    }
    static class UserBuyGoodsListReducer extends Reducer<Text,Text,Text,Text>{
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            Iterator<Text> iterator = values.iterator();
            StringBuilder sb = new StringBuilder();
            while(iterator.hasNext()) sb.append(iterator.next().toString() + ",");
            context.write(key,new Text(String.valueOf(sb.substring(0,sb.length()-1))));
        }
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run( new UserBuyGoodsList(),args));
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        Path in = new Path(conf.get("in"));
        Path out = new Path(conf.get("out"));
        Job job=Job.getInstance(conf,this.getClass().getSimpleName());
        job.setJarByClass(UserBuyGoodsList.class);
        job.setMapperClass(UserBuyGoodsListMapper.class);
        job.setReducerClass(UserBuyGoodsListReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        TextInputFormat.addInputPath(job,in);
        TextOutputFormat.setOutputPath(job,out);


        return job.waitForCompletion(true)?0:1;
    }
}
