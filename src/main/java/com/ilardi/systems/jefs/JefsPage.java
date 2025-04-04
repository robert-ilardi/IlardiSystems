/**
 * Created Feb 25, 2021
 */
package com.ilardi.systems.jefs;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author robert.ilardi
 *
 */

public class JefsPage implements Serializable {

  private final JefsPageHeader header;

  private byte[] data;

  public JefsPage(JefsPageHeader header) {
    this(header, null);
  }

  public JefsPage(JefsPageHeader header, byte[] data) {
    this.header = header;
    setData(data);
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  public JefsPageHeader getHeader() {
    return header;
  }

  public long getObjectId() {
    return header.getObjectId();
  }

  public long getPosition() {
    return header.getPosition();
  }

  public long getParentObjectId() {
    return header.getParentObjectId();
  }

  public JefsObjectType getObjectType() {
    return header.getObjectType();
  }

  public long getNextPagePosition() {
    return header.getNextPagePosition();
  }

  public String getObjectName() {
    return header.getObjectName();
  }

  public long getObjectPageIndex() {
    return header.getObjectPageIndex();
  }

  @Override
  public String toString() {
    return "JefsPage [header=" + header + ", data=" + Arrays.toString(data) + "]";
  }

  public boolean hasData() {
    return data != null;
  }

  public int getDataLength() {
    return (data != null ? data.length : 0);
  }

  public int getUsedDataSize() {
    return header.getUsedDataSize();
  }

  public byte[] getUsedDataOnly() {
    byte[] usedData = null;
    int usedDataSize;

    if (data != null) {
      usedDataSize = getUsedDataSize();
      usedData = new byte[usedDataSize];

      System.arraycopy(data, 0, usedData, 0, usedData.length);
    }

    return usedData;
  }

  public void setUsedDataSize(int usedDataSize) {
    header.setUsedDataSize(usedDataSize);
  }

}
