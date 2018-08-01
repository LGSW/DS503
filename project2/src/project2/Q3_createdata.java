package project2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class Q3_createdata {
	public static void main(String args[]) throws IOException{
		File file = new File("/home/hadoop/Desktop/proj2", "kmean_data_test");  
		try {  
			file.createNewFile();
		} catch (IOException e) {  
			// TODO Auto-generated catch block  
			e.printStackTrace();  
		}
		FileWriter fw = null;
	    BufferedWriter writer = null;
		try {  
			fw = new FileWriter(file);
	        writer = new BufferedWriter(fw);
	        try {  
	        	for(int i=1;i<=1000;i++){
	        		float x1=(float) (Math.random()*9999+1);
	        		float y1=(float) (Math.random()*9999+1);
	        		DecimalFormat df = new DecimalFormat("#.00");
	        	writer.write(df.format(x1)+","+df.format(y1));
	        	writer.newLine();
	        }
	        writer.flush();    
	        } catch (IOException e) {  
	        	// TODO Auto-generated catch block  
	        e.printStackTrace();  
	        }  
		} catch (FileNotFoundException e) {  
	    // TODO Auto-generated catch block  
	    e.printStackTrace();  
		}
		
	    
	}
	
}

