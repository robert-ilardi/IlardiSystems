/**
 * Created Apr 18, 2021
 */
package com.ilardi.systems.fileheap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;

import com.ilardi.systems.jefs.JefsPageHeader;

/**
 * @author robert.ilardi
 *
 */

public class VariableHeader implements Serializable {

  private JefsPageHeader jefsHeader;

  private int varHeaderLen;

  private String dataType;

  private int dataElementCnt;
  private int[] dataElementLens;

  private Properties properties;

  public VariableHeader() {}

  public VariableHeader(byte[] varHeaderBytes) throws IOException {
    ByteArrayOutputStream baos = null;
    ByteBuffer buf;
    byte b;
    byte[] bArr;
    String sTmp, name, value;
    String[] tokens;

    if (varHeaderBytes == null) {
      throw new IOException("Variable Header Bytes Array is NULL");
    }

    if (varHeaderBytes.length == 0) {
      throw new IOException("Variable Header Bytes Array is Empty");
    }

    buf = ByteBuffer.wrap(varHeaderBytes);

    // Read Header Length
    varHeaderLen = buf.getInt();

    if (varHeaderLen != varHeaderBytes.length) {
      throw new IOException("Variable Header Bytes Size Mismatch. Expected: " + varHeaderLen + "; Actual: " + varHeaderBytes.length);
    }

    // Read Data Type
    baos = new ByteArrayOutputStream();

    b = buf.get();

    while (b != 0) {
      baos.write(b);
      b = buf.get();
    }

    bArr = baos.toByteArray();

    dataType = new String(bArr);

    baos.close();
    baos = null;

    // Read Data Element Count
    dataElementCnt = buf.getInt();

    dataElementLens = new int[dataElementCnt];

    for (int i = 0; i < dataElementCnt; i++) {
      dataElementLens[i] = buf.getInt();
    }

    b = buf.get(); // Skip Start Text

    // Read Properties
    properties = new Properties();

    while (b != 3) {
      baos = new ByteArrayOutputStream();

      b = buf.get();

      while (b != 0 && b != 3) {
        baos.write(b);
        b = buf.get();
      }

      bArr = baos.toByteArray();

      if (bArr.length > 0) {
        sTmp = new String(bArr);

        baos.close();
        baos = null;

        tokens = sTmp.split("=");

        name = tokens[0].trim();
        value = tokens[1].trim();

        properties.setProperty(name, value);
      }
    }
  }

  public int getVarHeaderLen() {
    return varHeaderLen;
  }

  public void setVarHeaderLen(int varHeaderLen) {
    this.varHeaderLen = varHeaderLen;
  }

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public Properties getProperties() {
    return properties;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  public JefsPageHeader getJefsHeader() {
    return jefsHeader;
  }

  public void setJefsHeader(JefsPageHeader jefsHeader) {
    this.jefsHeader = jefsHeader;
  }

  public int getDataElementCnt() {
    return dataElementCnt;
  }

  public void setDataElementCnt(int dataElementCnt) {
    this.dataElementCnt = dataElementCnt;
  }

  public int[] getDataElementLens() {
    return dataElementLens;
  }

  public void setDataElementLens(int[] dataElementLens) {
    this.dataElementLens = dataElementLens;
  }

  public byte[] getVariableHeaderAsBytes() throws IOException {
    ByteArrayOutputStream baos = null;
    byte[] varHeaderBytes, bTmp;
    ByteBuffer buf;
    StringBuilder sb;
    String name, prop, tmpStr;
    Iterator<Object> iter;

    try {
      baos = new ByteArrayOutputStream();

      // Placeholder for Header Length
      bTmp = new byte[4];
      baos.write(bTmp);

      // Data Type
      bTmp = dataType.getBytes();
      baos.writeBytes(bTmp);

      baos.write(0); // Byte ZERO Delimiter

      // Data Element Count
      bTmp = new byte[4];
      buf = ByteBuffer.wrap(bTmp);
      buf.putInt(dataElementCnt);
      baos.write(bTmp);

      // Data Lengths
      if (dataElementLens != null) {
        for (int i = 0; i < dataElementCnt; i++) {
          bTmp = new byte[4];
          buf = ByteBuffer.wrap(bTmp);
          buf.putInt(dataElementLens[i]);
          baos.write(bTmp);
        }
      }

      // Write Properties

      baos.write(2); // Start Text Delimiter
      if (properties != null && !properties.isEmpty()) {
        iter = properties.keySet().iterator();

        while (iter.hasNext()) {
          name = (String) iter.next();
          prop = properties.getProperty(name);

          sb = new StringBuilder();

          sb.append(name.trim());
          sb.append("=");
          sb.append(prop.trim());

          tmpStr = sb.toString();

          bTmp = tmpStr.getBytes();

          baos.write(bTmp);
          baos.write(0); // Byte ZERO Delimiter
        }
      }

      baos.write(3); // End Text Delimiter

      // End Header----------->
      baos.write(30); // Record Separator / End Header

      // Convert to Byte Array
      varHeaderBytes = baos.toByteArray();
      buf = ByteBuffer.wrap(varHeaderBytes);

      // Set Header Length now that we know it
      varHeaderLen = varHeaderBytes.length;
      buf.putInt(varHeaderLen);

      return varHeaderBytes;
    }
    finally {
      if (baos != null) {
        baos.close();
        baos = null;
      }
    }
  }

  public int sumDataElementLengths() {
    int sum = 0;

    if (dataElementLens != null) {
      for (int i = 0; i < dataElementLens.length; i++) {
        sum += dataElementLens[i];
      }
    }

    return sum;
  }

  @Override
  public String toString() {
    return "VariableHeader [varHeaderLen=" + varHeaderLen + ", dataType=" + dataType + ", dataElementCnt=" + dataElementCnt + ", dataElementLens=" + Arrays.toString(dataElementLens) + ", properties="
        + properties + ", jefsHeader=" + jefsHeader + "]";
  }

}
