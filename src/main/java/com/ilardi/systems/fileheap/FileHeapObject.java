/**
 * Created Apr 6, 2021
 */
package com.ilardi.systems.fileheap;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * @author robert.ilardi
 *
 */

public class FileHeapObject<T extends Serializable> extends FileHeapBaseDataStructure<T> {

  private final Object fhoLock;

  public FileHeapObject() {
    super();

    fhoLock = new Object();
  }

  @Override
  public void updateVariableValue(T value) throws FileHeapException {
    synchronized (fhoLock) {
      VariableHeader varHeader;
      byte[] varHeaderBytes, varValue;

      try {
        varHeader = getVariableHeader(value);
        varValue = getVariableValueAsBytes(value, varHeader);

        varHeaderBytes = varHeader.getVariableHeaderAsBytes();

        memMan.updateVariableValue(variableName, jefsHeader, varHeaderBytes, varValue);
      }
      catch (Exception e) {
        throw new FileHeapException("An error occurred while attempting to Update Variable Value. System Message: " + e.getMessage(), e);
      }
    }
  }

  @Override
  public T retrieveVariableValue() throws FileHeapException {
    synchronized (fhoLock) {
      T retVal;
      VariableHeader varHeader;

      varHeader = memMan.retrieveVariableHeader(this);

      retVal = getVariableValue(varHeader);

      return retVal;
    }
  }

  @Override
  public VariableHeader getVariableHeader(T value) throws FileHeapException {
    synchronized (fhoLock) {
      VariableHeader varHeader;

      varHeader = new VariableHeader();
      varHeader.setDataType(value.getClass().getName());

      return varHeader;
    }
  }

  @Override
  public byte[] getVariableValueAsBytes(T value, VariableHeader varHeader) throws FileHeapException {
    synchronized (fhoLock) {
      byte[] varBytes;
      int[] dataElementLens;
      ByteBuffer bBuf;

      try {
        if (value instanceof String) {
          varBytes = value.toString().getBytes();
        }
        else if (value instanceof Byte) {
          varBytes = new byte[1];
          varBytes[0] = (Byte) value;
        }
        else if (value instanceof Boolean) {
          varBytes = new byte[1];
          varBytes[0] = (((Boolean) value).booleanValue() ? (byte) 1 : (byte) 0);
        }
        else if (value instanceof Character) {
          varBytes = new byte[Character.BYTES];
          bBuf = ByteBuffer.wrap(varBytes);
          bBuf.putChar((Character) value);
        }
        else if (value instanceof Short) {
          varBytes = new byte[Short.BYTES];
          bBuf = ByteBuffer.wrap(varBytes);
          bBuf.putShort((Short) value);
        }
        else if (value instanceof Integer) {
          varBytes = new byte[Integer.BYTES];
          bBuf = ByteBuffer.wrap(varBytes);
          bBuf.putInt((Integer) value);
        }
        else if (value instanceof Long) {
          varBytes = new byte[Long.BYTES];
          bBuf = ByteBuffer.wrap(varBytes);
          bBuf.putLong((Long) value);
        }
        else if (value instanceof Float) {
          varBytes = new byte[Float.BYTES];
          bBuf = ByteBuffer.wrap(varBytes);
          bBuf.putFloat((Float) value);
        }
        else if (value instanceof Double) {
          varBytes = new byte[Double.BYTES];
          bBuf = ByteBuffer.wrap(varBytes);
          bBuf.putDouble((Double) value);
        }
        else {
          varBytes = toByteArray(value);
        }

        varHeader.setDataElementCnt(1);

        dataElementLens = new int[1];
        dataElementLens[0] = varBytes.length;
        varHeader.setDataElementLens(dataElementLens);

        return varBytes;
      }
      catch (Exception e) {
        throw new FileHeapException("An error occurred while attempting to Convert Object to Bytes. System Message: " + e.getMessage(), e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public T getVariableValue(VariableHeader varHeader) throws FileHeapException {
    synchronized (fhoLock) {
      T retVal;
      String dataType;
      byte[] varBytes;
      ByteBuffer bBuf;

      try {
        varBytes = memMan.retrieveVariableData(this, varHeader);

        dataType = varHeader.getDataType();

        if (String.class.getName().equals(dataType)) {
          retVal = (T) new String(varBytes);
        }
        else if (Byte.class.getName().equals(dataType)) {
          retVal = (T) Byte.valueOf(varBytes[0]);
        }
        else if (Boolean.class.getName().equals(dataType)) {
          retVal = (T) Boolean.valueOf((varBytes[0] == (byte) 1));
        }
        else if (Character.class.getName().equals(dataType)) {
          bBuf = ByteBuffer.wrap(varBytes);
          retVal = (T) Character.valueOf(bBuf.getChar());
        }
        else if (Short.class.getName().equals(dataType)) {
          bBuf = ByteBuffer.wrap(varBytes);
          retVal = (T) Short.valueOf(bBuf.getShort());
        }
        else if (Integer.class.getName().equals(dataType)) {
          bBuf = ByteBuffer.wrap(varBytes);
          retVal = (T) Integer.valueOf(bBuf.getInt());
        }
        else if (Long.class.getName().equals(dataType)) {
          bBuf = ByteBuffer.wrap(varBytes);
          retVal = (T) Long.valueOf(bBuf.getLong());
        }
        else if (Float.class.getName().equals(dataType)) {
          bBuf = ByteBuffer.wrap(varBytes);
          retVal = (T) Float.valueOf(bBuf.getFloat());
        }
        else if (Double.class.getName().equals(dataType)) {
          bBuf = ByteBuffer.wrap(varBytes);
          retVal = (T) Double.valueOf(bBuf.getDouble());
        }
        else {
          retVal = toObject(varBytes);
        }

        return retVal;
      }
      catch (Exception e) {
        throw new FileHeapException("An error occurred while attempting to Convert Bytes to Object. System Message: " + e.getMessage(), e);
      }
    }
  }

}
