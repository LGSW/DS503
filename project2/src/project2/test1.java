package project2;

import java.io.BufferedInputStream;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.net.URI;  
import org.apache.hadoop.conf.Configuration;  
import org.apache.hadoop.fs.FSDataOutputStream;  
import org.apache.hadoop.fs.FileSystem;  
import org.apache.hadoop.fs.Path;  
import org.apache.hadoop.io.IOUtils;  
  
public class test1 {  
//********************************  
//把本地的一个文件拷贝到hdfs上  
//********************************  
    public static void main(String[] args) throws IOException {  
        String localSrc = "/home/hadoop/Desktop/proj2/kmean_data_test";  
        String dst = "hdfs://localhost:8020/user/hadoop/proj2/result";  
        InputStream in = new BufferedInputStream(new FileInputStream(localSrc));  
        Configuration conf = new Configuration();  
        FileSystem fs = FileSystem.get(URI.create(dst), conf);  
        FSDataOutputStream out = fs.create(new Path(dst));  
        IOUtils.copyBytes(in, out, conf, true);  
    }  
  
}  