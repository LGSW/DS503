package INputFormat2;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;
public class YaoUerRecordRead extends RecordReader<Text, user> {
    public static final String KEY_VALUE_SEPERATOR = 
            "mapreduce.input.keyvaluelinerecordreader.key.value.separator";
    private  LineRecordReader lineRecordReader;
    private byte separator = (byte)'\t';
    private Text key;
    private user value;
    private Text innerValue;

    public Class getKeyClass() { 
        return Text.class;
    }

    public YaoUerRecordRead(Configuration conf) {
        lineRecordReader = new LineRecordReader();
        String sepStr = conf.get(KEY_VALUE_SEPERATOR,"\t");
    this.separator = (byte) sepStr.charAt(0);
    }

    @Override
    public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        lineRecordReader.initialize(split, context);

    }

    public static void setKeyValue(Text key, user value, byte[] line,int lineLen, int pos) {
        if (pos == -1) {
            key.set(line, 0, lineLen);
            value.setAge(0);
            value.setName("");
            value.setSex("");
        } else {
            key.set(line, 0, pos);
            Text text=new Text();
            text.set(line, pos + 1, lineLen - pos - 1);
            System.out.println("text:"+  text.toString() );
            String[] userAll=text.toString().split(";");
            //User userInfo=new User();
            for (String u : userAll) {
                String k=u.split(":")[0];
                String v=u.split(":")[1];
                System.out.println(k+"==>"+v);
                if(k.equals("name")){
                    value.setName(v);
                }else if(k.equals("age")){
                    value.setAge(Integer.parseInt(v));
                }else if(k.equals("sex")){
                    value.setSex(v);
                }
            }
            System.out.println("setkv--value======="+value);
        }
    }
    
   
    
    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        byte[] line = null;
        int lineLen = -1;
        String s="";
        for(int i=0;i<15;i++){
        	if (lineRecordReader.nextKeyValue()) {
        		innerValue = lineRecordReader.getCurrentValue();
        		System.out.println("元数据innerValue："+innerValue);
        		s=s+innerValue.toString();
        	}
        	else {
                return false;
        		} 
        }
        for(int i=0;i<s.length();i++){
        	if(s.indexOf(i)=='{'|| s.indexOf(i)==' ' || s.indexOf(i)=='}'||s.indexOf(i)=='\t')
        		s=s.substring(0,i-1)+s.substring(i+1,s.length()-1);
        }
        Text in=new Text(s);
        System.out.println(in);
        line=in.getBytes();
		lineLen =in.getLength();
		
		
        if (line == null){
            return false;
        }
        if (key == null) {
            key = new Text();
        }
        if (value == null) {
            value = new user();
        }
        int pos = findSeparator(line, 0, lineLen, this.separator);
        setKeyValue(key, value, line, lineLen, pos);
        return true;
    }

    public static int findSeparator(byte[] utf, int start, int length, 
            byte sep) {
        for (int i = start; i < (start + length); i++) {
            if (utf[i] == sep) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public Text getCurrentKey() throws IOException, InterruptedException {
        return key;
    }

    @Override
    public user getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return lineRecordReader.getProgress();
    }

    @Override
    public void close() throws IOException {
        lineRecordReader.close();
    }

}