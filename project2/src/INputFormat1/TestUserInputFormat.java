package INputFormat1;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
public class TestUserInputFormat {
    public static class UserMapper extends Mapper<LongWritable,Text, LongWritable, Text>{
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, LongWritable, Text>.Context context) throws IOException, InterruptedException {
            context.write(key, value);
        }
    }

    public static void main(String[] args) {
        try {
            Configuration conf=new Configuration();
            Job job=Job.getInstance(conf,"Test lineRecordReader");
            job.setJarByClass(TestUserInputFormat.class);
            job.setInputFormatClass(TextInputFormat.class);
            job.setMapperClass(UserMapper.class);
            job.setOutputKeyClass(LongWritable.class);
            job.setOutputValueClass(Text.class);
            FileInputFormat.addInputPath(job, new Path("hdfs://localhost:8020/user/hadoop/proj2/input2"));
            FileOutputFormat.setOutputPath(job, new Path("hdfs://localhost:8020/user/hadoop/proj2/output2"));
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}