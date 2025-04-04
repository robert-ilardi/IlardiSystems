/**
 * Created Jan 24, 2021
 */
package com.ilardi.systems.net.intalk;

import java.nio.ByteBuffer;
import java.util.Arrays;

import com.ilardi.systems.net.IlardiNetException;

/**
 * @author rilardi
 *
 */

public class CommandDataPair {

  public static final byte[] MAGIC_BYTES = "INT6".getBytes();

  public static final int HEADER_LEN = 24;

  public static final int HEADER_POS_MAGIC_BYTES = 0;
  public static final int HEADER_POS_COMMAND = 4;
  public static final int HEADER_POS_MULTIPLEXER = 8;
  public static final int HEADER_POS_TRANSACTION_ID = 12;
  public static final int HEADER_POS_DATA_LEN = 16;
  public static final int HEADER_POS_DATA_COMPLETE_FLAG = 20;

  private int command;

  private int multiplexer;

  private int transactionId;

  private int dataLen;

  private byte[] data;

  private boolean dataComplete;

  public CommandDataPair() {
    dataLen = -1;
  }

  public CommandDataPair(byte[] header, byte[] data) throws IlardiNetException {
    this();

    ByteBuffer bb;
    byte[] magic;

    if (header == null || header.length < HEADER_LEN) {
      throw new IlardiNetException("Header Length is Invalid! Could NOT Construct Command Data Pair!");
    }

    bb = ByteBuffer.wrap(header);

    magic = new byte[MAGIC_BYTES.length];

    bb.get(magic);

    if (!Arrays.equals(MAGIC_BYTES, magic)) {
      throw new IlardiNetException("Magic Bytes Do NOT Match! Invalid Command Data Pair!");
    }

    command = bb.getInt();

    multiplexer = bb.getInt();

    transactionId = bb.getInt();

    dataLen = bb.getInt();

    dataComplete = (bb.get() == (byte) 1);

    this.data = data;
  }

  public int getCommand() {
    return command;
  }

  public void setCommand(int command) {
    this.command = command;
  }

  public int getMultiplexer() {
    return multiplexer;
  }

  public void setMultiplexer(int multiplexer) {
    this.multiplexer = multiplexer;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    if (dataLen == -1 && data != null) {
      dataLen = data.length;
    }

    this.data = data;
  }

  public boolean isDataComplete() {
    return dataComplete;
  }

  public void setDataComplete(boolean dataComplete) {
    this.dataComplete = dataComplete;
  }

  public int getDataLen() {
    return dataLen;
  }

  public void setDataLen(int dataLen) {
    this.dataLen = dataLen;
  }

  public byte[] getBytes() {
    byte[] bArr, tmp;
    ByteBuffer bb;

    bArr = new byte[HEADER_LEN + dataLen];

    bb = ByteBuffer.wrap(bArr);

    tmp = getHeaderBytes();
    bb.put(tmp);

    bb.put(data, 0, dataLen);

    return bArr;
  }

  public byte[] getHeaderBytes() {
    byte[] bArr;
    ByteBuffer bb;

    bArr = new byte[HEADER_LEN];

    bb = ByteBuffer.wrap(bArr);

    bb.put(MAGIC_BYTES);
    bb.putInt(command);
    bb.putInt(multiplexer);
    bb.putInt(transactionId);
    bb.putInt(dataLen);

    bb.put(dataComplete ? (byte) 1 : (byte) 0);
    bb.put((byte) 0);
    bb.put((byte) 0);
    bb.put((byte) 0);

    return bArr;
  }

  public int getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(int transactionId) {
    this.transactionId = transactionId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + command;
    result = prime * result + Arrays.hashCode(data);
    result = prime * result + (dataComplete ? 1231 : 1237);
    result = prime * result + dataLen;
    result = prime * result + multiplexer;
    result = prime * result + transactionId;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CommandDataPair other = (CommandDataPair) obj;
    if (command != other.command)
      return false;
    if (!Arrays.equals(data, other.data))
      return false;
    if (dataComplete != other.dataComplete)
      return false;
    if (dataLen != other.dataLen)
      return false;
    if (multiplexer != other.multiplexer)
      return false;
    if (transactionId != other.transactionId)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CommandDataPair [command=" + command + ", multiplexer=" + multiplexer + ", transactionId=" + transactionId + ", dataLen=" + dataLen + ", data=" + Arrays.toString(data) + ", dataComplete="
        + dataComplete + "]";
  }

}
