package INputFormat1;

import java.io.IOException; 
import java.io.InputStream; 
import org.apache.hadoop.classification.InterfaceAudience; 
import org.apache.hadoop.classification.InterfaceStability; 
import org.apache.hadoop.conf.Configuration; 
@InterfaceAudience.Private 
@InterfaceStability.Unstable 
public class SplitLineReader extends LineReader { 
public SplitLineReader(InputStream in, byte[] recordDelimiterBytes) { 
super(in, recordDelimiterBytes); 
}

  public SplitLineReader(InputStream in, Configuration conf,
      byte[] recordDelimiterBytes) throws IOException {
    super(in, conf, recordDelimiterBytes);
  }

  public boolean needAdditionalRecordAfterSplit() {
    return false;
  }
}