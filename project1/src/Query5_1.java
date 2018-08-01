import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class Query5_1 {

 

  public static class Map
       extends Mapper<Object, Text, Text, Text>{

    private Text line = new Text();

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
	line=value;
	String s[]= line.toString().split("\t");
	String str[]=s[1].split(",");
	if(Integer.parseInt(str[2])>10)
	  context.write(new Text(str[0]), new Text(""));
      }
    }

  
  public static class Reduce
	
       extends Reducer<Text,Text,Text,Text> {
	private Text line = new Text();
    public void reduce(Text key, Iterable<Text> values, Context context
                       ) throws IOException, InterruptedException {
       
       context.write(key,new Text(""));
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();

    Job job = new Job(conf, "Query5_1");
    job.setJarByClass(Query5_1.class);
    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);

    job.setOutputKeyClass(Text.class);
    //job.setNumReduceTasks(2);
    job.setOutputValueClass(Text.class);

    String inpath= "hdfs://localhost:8020/user/hadoop/proj1/output3/part-r-00000";
    String outpath="hdfs://localhost:8020/user/hadoop/proj1/output5_1";
    FileInputFormat.addInputPath(job, new Path(inpath));
    FileOutputFormat.setOutputPath(job, new Path(outpath));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }

  public static boolean isNumeric(String str){
    for (int i = 0; i < str.length(); i++){
        if (!Character.isDigit(str.charAt(i))){
          return false;
        }
    }
    return true;
  }

}
