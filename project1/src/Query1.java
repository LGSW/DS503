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

public class Query1 {

  public static class Map
       extends Mapper<Object, Text, Text, Text>{
    
    private Text word = new Text();
   
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {

    	StringTokenizer itr = new StringTokenizer(value.toString());
    	while (itr.hasMoreTokens()) {
    		word.set(itr.nextToken());
    		String s[]= word.toString().split(",");
    		Text x= new Text(s[0]+","+s[1]+","+s[3]);
	
    		if(Integer.parseInt(s[3])>=2 && Integer.parseInt(s[3])<=6)
    			context.write(x, new Text(""));
    	}
    }
  }
  
  public static class Reduce
       extends Reducer<Text,Text,Text,Text> {
    public void reduce(Text key, Iterable<IntWritable> values, 
                       Context context
                       ) throws IOException, InterruptedException {
      
      context.write(key, new Text(""));
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();

    Job job = new Job(conf, "Query1");
    job.setJarByClass(Query1.class);
    
    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    String inpath= "hdfs://localhost:8020/user/hadoop/proj1/input/Customers";
    String outpath="hdfs://localhost:8020/user/hadoop/proj1/output1-2";
    FileInputFormat.addInputPath(job, new Path(inpath));
    FileOutputFormat.setOutputPath(job, new Path(outpath));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
