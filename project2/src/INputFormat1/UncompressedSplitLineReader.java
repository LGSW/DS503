package INputFormat1;

import java.io.IOException;
import java.io.InputStream;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.io.Text;

/**
 * SplitLineReader for uncompressed files.
 * This class can split the file correctly even if the delimiter is multi-bytes.
 */
@InterfaceAudience.Private
@InterfaceStability.Unstable
public class UncompressedSplitLineReader extends SplitLineReader {
  private boolean needAdditionalRecord = false;
  private long splitLength;
  /** Total bytes read from the input stream. */
  private long totalBytesRead = 0;
  private boolean finished = false;
  private boolean usingCRLF;

  public UncompressedSplitLineReader(FSDataInputStream in, Configuration conf,
      byte[] recordDelimiterBytes, long splitLength) throws IOException {
    super(in, conf, recordDelimiterBytes);
    this.splitLength = splitLength;
    usingCRLF = (recordDelimiterBytes == null);
  }

  @Override
  protected int fillBuffer(InputStream in, byte[] buffer, boolean inDelimiter)
      throws IOException {
    int maxBytesToRead = buffer.length;
    if (totalBytesRead < splitLength) {
      maxBytesToRead = Math.min(maxBytesToRead,
                                (int)(splitLength - totalBytesRead));
    }
    int bytesRead = in.read(buffer, 0, maxBytesToRead);

    // If the split ended in the middle of a record delimiter then we need
    // to read one additional record, as the consumer of the next split will
    // not recognize the partial delimiter as a record.
    // However if using the default delimiter and the next character is a
    // linefeed then next split will treat it as a delimiter all by itself
    // and the additional record read should not be performed.
    if (totalBytesRead == splitLength && inDelimiter && bytesRead > 0) {
      if (usingCRLF) {
        needAdditionalRecord = (buffer[0] != '\n');
      } else {
        needAdditionalRecord = true;
      }
    }
    if (bytesRead > 0) {
      totalBytesRead += bytesRead;
    }
    return bytesRead;
  }

  @Override
  public int readLine(Text str, int maxLineLength, int maxBytesToConsume)
      throws IOException {
    int bytesRead = 0;
    if (!finished) {
      // only allow at most one more record to be read after the stream
      // reports the split ended
      if (totalBytesRead > splitLength) {
        finished = true;
      }

      bytesRead = super.readLine(str, maxLineLength, maxBytesToConsume);
    }
    return bytesRead;
  }

  @Override
  public boolean needAdditionalRecordAfterSplit() {
    return !finished && needAdditionalRecord;
  }

  @Override
  protected void unsetNeedAdditionalRecordAfterSplit() {
    needAdditionalRecord = false;
  }
}