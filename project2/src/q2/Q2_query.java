package q2;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class Q2_query {
	
	public static class Map extends Mapper<LongWritable, Text, Text, Text>{

		@Override
		protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			String s = value.toString();
			String str[] = s.split(",");
			String str2[]=str[5].split(":");
			
			context.write(new Text(str2[1]), new Text(""));
		}
	   
		
	}

	public static class Reduce extends Reducer<Text, Text, Text, Text>{

		@Override
		protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			int count =0;
			for(Text value:values){
			     count += 1;
			}
			context.write(key, new Text(Integer.toString(count)));
		}

	}

	
	

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		// TODO Auto-generated method stub
		Configuration configuration = new Configuration();
		//configuration.set("mapred.min.split.size","20000");
        //configuration.set("mapred.max.split.size","20000"); 
		
        Job job = Job.getInstance(configuration,"Query2");
        
        job.setJarByClass(Q2_query.class);
        job.setMapperClass(Map.class);
        
        job.setReducerClass(Reduce.class);
        job.setMapOutputKeyClass(Text.class);
	    job.setMapOutputValueClass(Text.class);
	    job.setInputFormatClass(CusTextInputFormat.class);
	    job.setOutputFormatClass(TextOutputFormat.class);
	     FileInputFormat.setInputPaths(job, new Path("hdfs://localhost:8020/user/hadoop/proj2/input2"));
	     FileOutputFormat.setOutputPath(job, new Path("hdfs://localhost:8020/user/hadoop/proj2/output2"));
	     
	     System.out.println("Map number ="+configuration.get("mapred.map.tasks"));
	     
	     System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
	

}
