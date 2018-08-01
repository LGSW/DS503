package q2;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;

public class CusRecordReader extends RecordReader<LongWritable, Text>{
    
	private LineRecordReader lineRecordReader = new LineRecordReader();
	private LongWritable key = new LongWritable();
	private Text value = new Text();
	
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		lineRecordReader.close();
	}

	@Override
	public LongWritable getCurrentKey() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return this.key;
	}

	@Override
	public Text getCurrentValue() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return this.value;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return lineRecordReader.getProgress();
	}

	@Override
	public void initialize(InputSplit arg0, TaskAttemptContext arg1) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		lineRecordReader.initialize(arg0, arg1);
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		// TODO Auto-generated method stub

		
		boolean end_of_file = lineRecordReader.nextKeyValue();
		if (end_of_file == false) {
			return end_of_file;
		}else {
			HashMap<String,String > content_value =  new HashMap<String, String>();
			if (!lineRecordReader.getCurrentValue().toString().contains("{")) {
				System.err.println("Wrong begining signal");
				return false;
			}else {
				while(lineRecordReader.nextKeyValue()){
					if (lineRecordReader.getCurrentValue().toString().contains("}")) {
						break;
					}else {
						String[] split_record = lineRecordReader.getCurrentValue().toString().split(":");
						content_value.put(split_record[0].replace("\"", "").trim(), 
								split_record[1].replace("\"", "").replace(",", "").trim());
					}
				}
				key = lineRecordReader.getCurrentKey();
				value.set("ID:"+content_value.get("ID")+","+"ShortName:"+content_value.get("ShortName")+","
						+"Name:"+content_value.get("Name")+","+"Region:"+content_value.get("Region")+","
						+"ICAO:"+content_value.get("ICAO")+","+"Flags:"+content_value.get("Flags")+","
						+"Catlog:"+content_value.get("Catalog")+","+"Length:"+content_value.get("Length")+","
						+"Elevation:"+content_value.get("Elevation")+","+"Runway:"+content_value.get("Runway")+","
						+"Frequency:"+content_value.get("Frequency")+","+"Latitude:"+content_value.get("Latitude")+","
						+"Longtitude:"+content_value.get("Longitude"));  
			    
				return true;
			}
		}
		

	
	}

}
