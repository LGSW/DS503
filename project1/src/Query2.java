import java.io.IOException;
import java.util.StringTokenizer;

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

public class Query2 {

  public static class Map
       extends Mapper<Object, Text, Text, Text>{
    
    private static Text word = new Text();
      
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	
    	StringTokenizer itr = new StringTokenizer(value.toString());
    	while (itr.hasMoreTokens()) {
    		word.set(itr.nextToken());
            String s[]= word.toString().split(",");
            String CustID= s[1];
            String TransTotal= s[2];
            context.write(new Text(CustID), new Text("1,"+ TransTotal));  
    	}
    }
  }
  public static class Combine
       extends Reducer<Text,Text, Text, Text> {

    public void reduce(Text key, Iterable<Text> values, Context context
                       ) throws IOException, InterruptedException {
      double sum = 0;
      int count =0;
      for (Text value : values) {
	String ss[] = value.toString().split(","); 
	count+=Integer.parseInt(ss[0]);
	sum += Double.parseDouble(ss[1]);
	
      }
	String w=count+","+sum;
	context.write(key, new Text(w));
    }
  }

  public static class Reduce
       extends Reducer<Text,Text, Text, Text> {

    public void reduce(Text key, Iterable<Text> values, Context context
                       ) throws IOException, InterruptedException {
      double sum = 0;
      int count =0;
      for (Text value : values) {
	String ss[] = value.toString().split(","); 
	count+=Integer.parseInt(ss[0]);
	sum += Double.parseDouble(ss[1]);
      }
	String w=count+","+sum;
	context.write(key, new Text(w));
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration(); 
    Job job = new Job(conf, "word count");
    job.setJarByClass(Query2.class);
    job.setMapperClass(Map.class);
    job.setCombinerClass(Combine.class);
    job.setReducerClass(Reduce.class);

    job.setOutputKeyClass(Text.class);
    //job.setNumReduceTasks(2);
    job.setOutputValueClass(Text.class);
    String inpath= "hdfs://localhost:8020/user/hadoop/proj1/input1/transaction";
    String outpath="hdfs://localhost:8020/user/hadoop/proj1/output2";
    FileInputFormat.addInputPath(job, new Path(inpath));
    FileOutputFormat.setOutputPath(job, new Path(outpath));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
