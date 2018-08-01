package INputFormat2;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
public class YaoUerInputFormat extends FileInputFormat<Text,user> {
    @Override
    public RecordReader<Text, user> createRecordReader(InputSplit split, TaskAttemptContext context)
            throws IOException, InterruptedException {
        context.setStatus(split.toString());
        return new YaoUerRecordRead(context.getConfiguration());
    }

}