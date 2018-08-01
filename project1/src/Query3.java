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

public class Query3 {

  public static class Map
       extends Mapper<Object, Text, Text, Text>{
    
    private Text line = new Text();
   
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	
    StringTokenizer itr = new StringTokenizer(value.toString());
    while (itr.hasMoreTokens()) {
    	line.set(itr.nextToken());
    	Text x;
    	Text y;
    	String s[]= line.toString().split(",");
    	if(isNumeric(s[1])){
    		y= new Text(s[0]+","+s[2]+","+s[3]+","+s[4]+",0");
    		x= new Text(s[1]);
    	}
    	else{
    		y= new Text(s[1]+","+s[2]+","+s[3]+","+s[4]+",1");
    		x= new Text(s[0]);
    	}	
        context.write(x, y);
      }
    }
    }
  
  public static class Reduce
       extends Reducer<Text,Text,Text,Text> {
	private Text kw = new Text();
	private Text out = new Text();

    public void reduce(Text key, Iterable<Text> values, Context context
                       ) throws IOException, InterruptedException {
       String name="";
       String salary="";
       float TotalSum = 0;
       int num=0;
       int minitem=20;
       for (Text value : values) {
         String str[] = value.toString().split(",");
	 if(str[4].equals("1")){
	   name = str[0];
	   salary = str[3];
	 }
	 else if(str[4].equals("0")){
	   num++;
           TotalSum += Float.parseFloat(str[1]);
	   if(Integer.parseInt(str[2])<minitem)
	     minitem=Integer.parseInt(str[2]);
	 }
       }
       
       out.set(name+","+salary+","+num+","+TotalSum+","+minitem);
       context.write(key,out);	
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();

    Job job = new Job(conf, "Query3");
    job.setJarByClass(Query3.class);
    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);

    job.setOutputKeyClass(Text.class);
    //job.setNumReduceTasks(2);
    job.setOutputValueClass(Text.class);
    String inpath= "hdfs://localhost:8020/user/hadoop/proj1/input1";
    String outpath="hdfs://localhost:8020/user/hadoop/proj1/output3";
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
