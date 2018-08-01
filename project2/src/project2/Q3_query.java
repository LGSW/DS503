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
import java.io.InputStreamReader;
import java.net.URI;
import java.text.DecimalFormat;

import org.apache.hadoop.fs.FSDataInputStream;
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



public class Q3_query{

  public static class Map
       extends Mapper<Object, Text, Text, Text>{
    
    private Text word = new Text();
    //double a[]={3000,7000};
    //double b[]={3000,7000};
    
    
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	
    	Vector<String> vecA = new Vector<String>();
        Vector<String> vecB = new Vector<String>();
    	int k=Integer.parseInt(context.getConfiguration().get("kvalue"));
    	for(int i=0;i<k;i++){
    		vecA.add(context.getConfiguration().get("centx"+i));
    		vecB.add(context.getConfiguration().get("centy"+i));
    	}
    	
    	
    	StringTokenizer itr = new StringTokenizer(value.toString());
    	while (itr.hasMoreTokens()) {
    		word.set(itr.nextToken());
    		String ss[]= word.toString().split("\t");
    		String s[]=ss[0].split(",");
    		double minlen=99999;
    		int index=0;
    		for(int i=0;i<k;i++){
    			double disx=Math.pow(Double.parseDouble(vecA.get(i))-Double.parseDouble(s[0]),2);
    			double disy=Math.pow(Double.parseDouble(vecB.get(i))-Double.parseDouble(s[1]),2);
    			
    			if((Math.sqrt(disx+disy))<minlen){
    				minlen=Math.sqrt(disx+disy);
    				index=i;
    			}
    		}
    		context.write(new Text(vecA.get(index)+","+vecB.get(index)), new Text(s[0]+","+s[1]));
    	}
    	
     }
    	
  }
  public static class Combiner extends Reducer<Text,Text,Text,Text> {
	  double sumx;
	  double sumy;
	  
	  public void reduce(Text key, Iterable<Text> values, 
                       Context context) throws IOException, InterruptedException {
		  double sumx=0.0;
		  double sumy=0.0;
		  int count=0;
		  for(Text value:values){
			  String s[]=value.toString().split(",");			  
			  sumx+=Double.parseDouble(s[0]);
			  sumy+=Double.parseDouble(s[1]);
			  count++;
		  }
		  context.write(key, new Text(sumx+","+sumy+","+count));
	  }
  }
  public static class Reduce extends Reducer<Text,Text,Text,Text> {
	  double sumx;
	  double sumy;
	  
	  public void reduce(Text key, Iterable<Text> values, 
                       Context context) throws IOException, InterruptedException {
		  double sumx=0.0;
		  double sumy=0.0;
		  int count=0;
		  for(Text value:values){
			  String s[]=value.toString().split(",");
			  count+=Integer.parseInt(s[2]);
			  sumx+=Double.parseDouble(s[0]);
			  sumy+=Double.parseDouble(s[1]);
		  }
		  double avx=sumx/count;
		  double avy=sumy/count;
		  String ss[]=key.toString().split(",");
		  String yn="N";
		  if(Math.abs(avx-Double.parseDouble(ss[0]))<10 && Math.abs(avy-Double.parseDouble(ss[1]))<10)
			 yn="Y";
		  context.write(new Text(avx+","+avy),new Text(ss[0]+","+ss[1]+","+yn));
		  
    }
  }

  
  public static void main(String[] args) throws Exception {  ////////////////////////////////////////////  main
	
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
        		double x1=(double) (Math.random()*2000-1000);
        		double y1=(double) (Math.random()*2000-1000);
        		//DecimalFormat df = new DecimalFormat("#.00");
        	//writer.write(df.format(x1)+","+df.format(y1)+"\n");
        	writer.write(x1+","+y1+"\n");
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
     String dst ="hdfs://localhost:8020/user/hadoop/proj2/Q3_0/part-r-00000";  
     InputStream in = new BufferedInputStream(new FileInputStream(localSrc));  
     Configuration conf1 = new Configuration();  
     FileSystem fs = FileSystem.get(URI.create(dst), conf1);  
     FSDataOutputStream out = fs.create(new Path(dst));  
     IOUtils.copyBytes(in, out, conf1, true);  
	////////////////////////////////////////////////////////
	
	
    String INPUT_PATH1 = "hdfs://localhost:8020/user/hadoop/proj2/Q3_0/part-r-00000";  //center data
    String INPUT_PATH2 = "hdfs://localhost:8020/user/hadoop/proj2/input3_zhou";  //K-mean data
    String OUT_PATH = "hdfs://localhost:8020/user/hadoop/proj2/Q3_1";  //new center data
    
    
    int it=0;
    boolean converge=false;
    
    while(it<5 && !converge){
    Configuration conf = new Configuration();
    Path path = new Path(INPUT_PATH1); 
    FileSystem fs1 = FileSystem.get(conf);
     FSDataInputStream fsin= fs.open(path); 
     BufferedReader br =null;
     String line ;
     Vector<String> vecA = new Vector<String>();
     Vector<String> vecB = new Vector<String>();
     int s=0;
     try{
      br = new BufferedReader(new InputStreamReader(fsin));
         while ((line = br.readLine()) != null) {
           //System.out.println(line);
           String strr[]=line.split("\t");
           String str[]=strr[0].split(",");
           vecA.add(str[0]);
           vecB.add(str[1]);
           if(it!=0){
        	   //System.out.println(strr[1].split(",")[2]);
        	   if(strr[1].split(",")[2].equals("Y"))
        		   s++;
           } 
         }
     }finally{
      br.close();
     }
     
     k=vecA.size();
     System.out.println(k);
     System.out.println(s);
     if(s==k)
    	 converge=true;
    for(int i=0;i<k;i++){
    	System.out.println(vecA.get(i)+","+vecB.get(i));
    }
    conf.set("kvalue",k+"");
    for(int i=0;i<k;i++){
    	conf.set("centx"+i, vecA.get(i)+"");
    	conf.set("centy"+i, vecB.get(i)+"");
    }
    conf.set("kvalue",k+"");
    
    Job job = new Job(conf,"kmean");  
    job.setJarByClass(Q3_query.class);  
    FileInputFormat.setInputPaths(job, INPUT_PATH2);  
    job.setMapperClass(Map.class);
	job.setCombinerClass(Combiner.class);
	job.setReducerClass(Reduce.class);
	job.setNumReduceTasks(1); 
    job.setMapOutputKeyClass(Text.class);  
    job.setMapOutputValueClass(Text.class);  
    FileOutputFormat.setOutputPath(job, new Path(OUT_PATH));  
    job.waitForCompletion(true);
    //System.exit(job.waitForCompletion(true) ? 0 : 1);  
    INPUT_PATH1=OUT_PATH+"/part-r-00000";
    OUT_PATH=OUT_PATH.substring(0,OUT_PATH.length()-1)+(it+2)+"";
    it++;
    }
    
  }

}
