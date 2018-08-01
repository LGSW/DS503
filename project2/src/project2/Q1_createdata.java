package project2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class Q1_createdata {
	public static void main(String args[]) throws IOException{
		File file = new File("/home/hadoop/Desktop/proj2", "P");  
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
	        	for(int i=1;i<=10000000;i++){
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
		
	    File file2 = new File("/home/hadoop/Desktop/proj2", "R");  
		try {  
			file2.createNewFile();   
		} catch (IOException e2) {  
			// TODO Auto-generated catch block  
			e2.printStackTrace();  
		}
		FileWriter fw2 = null;
	    BufferedWriter writer2 = null;
		try {  
			fw2 = new FileWriter(file2);
	        writer2 = new BufferedWriter(fw2);
	        try {  
	        	for(int i=1;i<=6000000;i++){
	        		
	        		float x2=(float)(Math.random()*9999+1);
	        		float y2=(float)(Math.random()*9999+1);
	        		float w=(float)(Math.random()*4+1);
	        		float h=(float)(Math.random()*19+1);
	        		DecimalFormat df = new DecimalFormat("#.00");  
	        	writer2.write(df.format(x2)+","+df.format(y2)+","+df.format(w)+","+df.format(h));
	        	writer2.newLine();
	        }
	        writer2.flush();  
	        
	        } catch (IOException e2) {  
	        	// TODO Auto-generated catch block  
	        e2.printStackTrace();  
	        }  
		} catch (FileNotFoundException e2) {  
	    // TODO Auto-generated catch block  
	    e2.printStackTrace();  
		}
	}
	
}

