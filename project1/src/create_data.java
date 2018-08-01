import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class create_data {
	public static void main(String args[]) throws IOException{
		File file = new File("/home/hadoop/Desktop/proj1", "Customers");  
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
	        	for(int i=1;i<=50000;i++){
	        		int ID=i;
	        		int len=(int)(Math.random()*11+10);
	        		String name=getRandomString(len);
	        		int age=(int)(Math.random()*61+10);
	        		int CountryCode=(int)(Math.random()*10+1);
	        		float salary=(float) (Math.random()*9901+100);
	        	writer.write(ID+","+ name+","+age+","+CountryCode +","+String.format("%.2f",salary));
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
		
	    File file2 = new File("/home/hadoop/Desktop/proj1", "Transactions");  
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
	        	for(int i=1;i<=5000000;i++){
	        		int transID=i;
	        		int CustID=(int)(Math.random()*50000+1);
	        		float TransTotal=(float)(Math.random()*991+10);
	        		int TransNumItems=(int)(Math.random()*10+1);
	        		int len=(int)(Math.random()*11+10);
	        		String TransDesc=getRandomString(len);
	        	writer2.write(transID+","+ CustID+","+String.format("%.2f",TransTotal)+","+TransNumItems +","+TransDesc);
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
	public static String getRandomString(int length) {   
	    String base = "abcdefghijklmnopqrstuvwxyz";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	}     
}

