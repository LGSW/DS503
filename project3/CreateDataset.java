package com.cs585.project2.task1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class CreateDataset {

	public static void main(String[] args){

		try{
			FileWriter points = new FileWriter("points.txt");
		
	        for (int i = 1; i<10000000:i++){
	        	float x = new Random().nextFloat()*10000 + 1;
	        	float y = new Random().nextFloat()*10000 + 1;
	        	String point = x + " " + y +"\r\n";
	        	points.write(point);
	        }
	        points.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}
}
    
