/**
 * Created Apr 6, 2021
 */
package com.ilardi.systems.fileheap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.ilardi.systems.jefs.JefsPageHeader;

/**
 * @author robert.ilardi
 *
 */

public abstract class FileHeapBaseDataStructure<T extends Serializable> implements FileHeapDataStructure<T> {

  protected MemoryManager memMan;

  protected String variableName;

  protected JefsPageHeader jefsHeader;

  public FileHeapBaseDataStructure() {}

  @Override
  public void setMemoryManager(MemoryManager memMan) {
    this.memMan = memMan;
  }

  @Override
  public MemoryManager getMemoryManager() {
    return memMan;
  }

  @Override
  public void setVariableName(String variableName) {
    this.variableName = variableName;
  }

  @Override
  public String getVariableName() {
    return variableName;
  }

  @Override
  public void setJefsHeader(JefsPageHeader jefsHeader) {
    this.jefsHeader = jefsHeader;
  }

  @Override
  public JefsPageHeader getJefsHeader() {
    return jefsHeader;
  }

  public byte[] toByteArray(T obj) throws IOException {
    ByteArrayOutputStream baos = null;
    ObjectOutputStream oos = null;
    byte[] bin = null;

    try {
      baos = new ByteArrayOutputStream();
      oos = new ObjectOutputStream(baos);

      oos.writeObject(obj);

      bin = baos.toByteArray();
    }
    finally {
      if (oos != null) {
        try {
          oos.close();
        }
        catch (Exception e) {}
        oos = null;
      }

      if (baos != null) {
        try {
          baos.close();
        }
        catch (Exception e) {}
        baos = null;
      }
    }

    return bin;
  }

  @SuppressWarnings("unchecked")
  public T toObject(byte[] objBin) throws IOException, ClassNotFoundException {
    T obj = null;
    ByteArrayInputStream bais = null;
    ObjectInputStream ois = null;

    try {
      bais = new ByteArrayInputStream(objBin);
      ois = new ObjectInputStream(bais);
      obj = (T) ois.readObject();
    }
    finally {
      if (ois != null) {
        try {
          ois.close();
        }
        catch (Exception e) {}
        ois = null;
      }

      if (bais != null) {
        try {
          bais.close();
        }
        catch (Exception e) {}
        bais = null;
      }
    }

    return obj;
  }

}
