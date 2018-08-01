package project2;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

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

public class Q1_query{

  public static class Map
       extends Mapper<Object, Text, Text, Text>{
    
    private Text word = new Text();
    
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	
    	Double x1=Double.parseDouble(context.getConfiguration().get("x1"));
    	Double y1=Double.parseDouble(context.getConfiguration().get("y1"));
    	Double x2=Double.parseDouble(context.getConfiguration().get("x2"));
    	Double y2=Double.parseDouble(context.getConfiguration().get("y2"));
    	int wpar=(int)(Math.abs(x2-x1)/20)+1;
    	int hpar=(int)(Math.abs(y2-y1)/20)+1;
    	double m=(x2-x1)/wpar;
    	double n=(y2-y1)/hpar;
    	
    	StringTokenizer itr = new StringTokenizer(value.toString());
    	while (itr.hasMoreTokens()) {
    		word.set(itr.nextToken());
    		String s[]= word.toString().split(",");
    		if(s.length==2){
    			double px=Double.parseDouble(s[0]);
    			double py=Double.parseDouble(s[1]);
    			if(px>=x1 && px<=x2 && py>=y1 && py<=y2){
    				int xi=(int)((px-x1)/m);
    				int yi=(int)((py-y1)/n);
    				double ldx=x1+xi*m;
    				double ldy=y1+yi*n;
    				double rux=x1+(xi+1)*m;
    				double ruy=y1+(yi+1)*n;
    				context.write(new Text(ldx+","+ldy+","+rux+","+ruy), new Text("0,"+px+","+py));
    			}
    		}
    		else if(s.length==4){
    			double px1=Double.parseDouble(s[0]);
    			double py1=Double.parseDouble(s[1]);
    			double w=Double.parseDouble(s[2]);
    			double h=Double.parseDouble(s[3]);
    			double a1=px1;
    			double b1=py1-h;	
    			double a2=px1+w;
    			double b2=py1;
    			double zx=Math.abs(a1+a2-x1-x2);
    			double zy=Math.abs(b1+b2-y1-y2);
    			double xh=a2-a1+x2-x1;
    			double yh=b2-b1+y2-y1;
    			
    			if(zx <= xh && zy <= yh){
    				for(int i=0;i<wpar;i++){
    					for(int j=0;j<hpar;j++){
    						double rx1=x1+m*i;
    						double ry1=y1+n*j;
    						double rx2=x1+m*(i+1);
    						double ry2=x1+n*(j+1);
    						double nzx=Math.abs(a1+a2-rx1-rx2);
    		    			double nzy=Math.abs(b1+b2-ry1-ry2);
    		    			double nxh=a2-a1+rx2-rx1;
    		    			double nyh=b2-b1+ry2-ry1;
    		    			if(nzx <= nxh && nzy <= nyh){
    		    				context.write(new Text(rx1+","+ry1+","+rx2+","+ry2), new Text("1,"+px1+","+py1+","+w+","+h));
    		    			}
    					}
    				}
    			}

    		}
    	}
    }
  }
  
  public static class Reduce
       extends Reducer<Text,Text,Text,Text> {
    public void reduce(Text key, Iterable<Text> values, 
                       Context context
                       ) throws IOException, InterruptedException {
    	
    	  Vector<String> vecA = new Vector<String>();
          Vector<String> vecB = new Vector<String>();
    	
          for(Text value:values){
        	  String s =value.toString();
              if(s.startsWith("1")) {
                  vecA.add(s.substring(2));
              } else if(s.startsWith("0")) {
                  vecB.add(s.substring(2));
              }
          }
          
          for (int i = 0;i<vecA.size();i++) {
        	  for (int j = 0; j < vecB.size(); j++) {
        		  context.write(new Text("R"+(i+1)) , new Text(vecB.get(j)));
              }
          }
          
    }
  }

  public static void main(String[] args) throws Exception {
	
    Configuration conf = new Configuration();
    conf.setInt("x1", Integer.parseInt(args[0]));
    conf.setInt("y1", Integer.parseInt(args[1]));
    conf.setInt("x2", Integer.parseInt(args[2]));
    conf.setInt("y2", Integer.parseInt(args[3]));
    
    Job job = new Job(conf, "Query1");
    job.setJarByClass(Q1_query.class);
    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    String inpath= "hdfs://localhost:8020/user/hadoop/proj2/input1";
    String outpath="hdfs://localhost:8020/user/hadoop/proj2/output1";
    FileInputFormat.addInputPath(job, new Path(inpath));
    FileOutputFormat.setOutputPath(job, new Path(outpath));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
