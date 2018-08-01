package project2;


import java.util.StringTokenizer;
import java.util.Vector;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;  
import java.io.InputStream;  
import java.net.URI;
import java.text.DecimalFormat;

import org.apache.hadoop.fs.FSDataOutputStream;  
import org.apache.hadoop.fs.FileSystem;  
import org.apache.hadoop.io.IOUtils;  
import org.apache.hadoop.util.Progressable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;



public class test2{

  public static class Map
       extends Mapper<Object, Text, Text, Text>{
    
    private Text word = new Text();

    Vector<String> vecA = new Vector<String>();
    Vector<String> vecB = new Vector<String>();
    
    public void setup(Object key, Text value, Context context
            ) throws IOException, InterruptedException { 

        Path[] paths = DistributedCache.getLocalCacheFiles(context.getConfiguration());  
        BufferedReader reader = new BufferedReader(new FileReader(paths[0].toString()));  

        String str = null;  
        try {  
            // 一行一行读取  
            while ((str = reader.readLine()) != null) {  
                // 对缓存中的表进行分割  
                String[] splits = str.split(",");  
                // 把字符数组中有用的数据存在一个Map中  
                vecA.add(splits[0]);
                vecB.add(splits[1]);
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally{  
            reader.close();  
        }  

    }  
    
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	
    	StringTokenizer itr = new StringTokenizer(value.toString());
    	while (itr.hasMoreTokens()) {
    		
    		context.write(new Text(vecA.size()+""), new Text(vecB.size()+""));
    	}
    	
     }
    	
  }
  
  public static class Reduce extends Reducer<Text,Text,Text,Text> {
	  
	  public void reduce(Text key, Iterable<Text> values, 
                       Context context) throws IOException, InterruptedException {
		 
	  context.write(key,new Text(""));
		  
    }
  }

  
  public static void main(String[] args) throws Exception {  ////////////  main
	
	int k=Integer.parseInt(args[0]);  
	
	//////////////////////////////// create a file in a local path
	File file = new File("/home/hadoop/Desktop/proj2", "centers_data");  
	try {  
		file.createNewFile();
	} catch (IOException e) {   
		e.printStackTrace();  
	}
	FileWriter fw = null;
    BufferedWriter writer = null;
	try {  
		fw = new FileWriter(file);
        writer = new BufferedWriter(fw);
        try {  
        	for(int i=1;i<=k;i++){
        		float x1=(float) (Math.random()*9999+1);
        		float y1=(float) (Math.random()*9999+1);
        		DecimalFormat df = new DecimalFormat("#.00");
        	writer.write(df.format(x1)+","+df.format(y1));
        	writer.newLine();
        }
        writer.flush();    
        } catch (IOException e) {   
        e.printStackTrace();  
        }  
	} catch (FileNotFoundException e) {  
    e.printStackTrace();  
	}
	//////////////////////////////////////////////////////////
	
	////////////////////////////  put center points dataset to HDFS
	 String localSrc ="/home/hadoop/Desktop/proj2/centers_data";  
     String dst ="hdfs://localhost:8020/user/hadoop/proj2/Q3_0";  
     InputStream in = new BufferedInputStream(new FileInputStream(localSrc));  
     Configuration conf1 = new Configuration();  
     FileSystem fs = FileSystem.get(URI.create(dst), conf1);  
     FSDataOutputStream out = fs.create(new Path(dst));  
     IOUtils.copyBytes(in, out, conf1, true);  
	////////////////////////////////////////////////////////
	
	
    String INPUT_PATH1 = "hdfs://localhost:8020/user/hadoop/proj2/Q3_0";  
    String INPUT_PATH2 = "hdfs://localhost:8020/user/hadoop/proj2/input3";  
    String OUT_PATH = "hdfs://localhost:8020/user/hadoop/proj2/Q3_1";  

    FileSystem fileSystem = FileSystem.get(new URI(OUT_PATH), conf1);   
    if ((fileSystem).exists(new Path(OUT_PATH))) {  
        fileSystem.delete(new Path(OUT_PATH), true);  
    }  
    DistributedCache.addCacheFile(new Path(INPUT_PATH1).toUri(), conf1); 
    
    Job job = new Job(conf1,Q3_query.class.getName());  
    job.setJarByClass(Q3_query.class);  
    FileInputFormat.setInputPaths(job, INPUT_PATH1);  
    //job.setInputFormatClass(TextInputFormat.class);  
    job.setMapperClass(Map.class);
	//job.setCombinerClass(Combiner.class);
	job.setReducerClass(Reduce.class);
	job.setNumReduceTasks(1); 
    
    
    job.setMapOutputKeyClass(Text.class);  
    job.setMapOutputValueClass(Text.class);  
    job.setNumReduceTasks(0);  

    FileOutputFormat.setOutputPath(job, new Path(OUT_PATH));   
    System.exit(job.waitForCompletion(true) ? 0 : 1);  

    
  }

}
