/**
 * Created May 2, 2022
 */
package com.ilardi.systems.jefs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author robert.ilardi
 *
 */

public class PhysicalRandomAccessFile {

  protected RandomAccessFile physRaf;

  protected Object pfLock;

  public PhysicalRandomAccessFile(String name, String mode) throws FileNotFoundException {
    pfLock = new Object();
    physRaf = new RandomAccessFile(name, mode);
  }

  public void close() throws IOException {
    synchronized (pfLock) {
      if (physRaf != null) {
        try {
          physRaf.close();
        }
        finally {
          physRaf = null;
        }
      }
    }
  }

  public void setLength(int len) throws IOException {
    synchronized (pfLock) {
      if (physRaf != null) {
        physRaf.setLength(len);
      }
      else {
        throw new IOException("Physical File NOT Opened!");
      }
    }
  }

  public long length() throws IOException {
    synchronized (pfLock) {
      if (physRaf != null) {
        return physRaf.length();
      }
      else {
        throw new IOException("Physical File NOT Opened!");
      }
    }
  }

  public void seek(long pos) throws IOException {
    synchronized (pfLock) {
      if (physRaf != null) {
        physRaf.seek(pos);
      }
      else {
        throw new IOException("Physical File NOT Opened!");
      }
    }
  }

  public void write(byte[] bArr) throws IOException {
    synchronized (pfLock) {
      if (physRaf != null) {
        physRaf.write(bArr);
      }
      else {
        throw new IOException("Physical File NOT Opened!");
      }
    }
  }

  public int read(byte[] data) throws IOException {
    synchronized (pfLock) {
      if (physRaf != null) {
        return physRaf.read(data);
      }
      else {
        throw new IOException("Physical File NOT Opened!");
      }
    }
  }

}
