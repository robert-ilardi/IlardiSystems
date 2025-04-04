/**
 * Created Jan 9, 2021
 */
package com.ilardi.systems.net;

import java.io.InputStream;
import java.net.Socket;

/**
 * @author rilardi
 *
 */

public class BlockingSocketReader extends BaseSocketReader {

  private static final int READ_BUF_SIZE = 2048;

  private Socket clientSocket;

  private InputStream socketIns;

  private byte[] readBuffer;

  public BlockingSocketReader() {
    super();
  }

  public void setClientSocket(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  @Override
  public void init() throws IlardiNetException {
    synchronized (readerLock) {
      System.out.println("BlockingSocketReader Executing Init");

      try {
        readBuffer = new byte[READ_BUF_SIZE];

        socketIns = clientSocket.getInputStream();
      } // End try block
      catch (Exception e) {
        throw new IlardiNetException("An error occurred while attempting Initialize Blocking Socket Reader! System Message: " + e.getMessage(), e);
      }
    }
  }

  @Override
  public void close() throws IlardiNetException {
    synchronized (readerLock) {
      System.out.println("BlockingSocketReader Executing Shutdown");

      try {
        socketIns.close();
        socketIns = null;

        readBuffer = null;
        clientSocket = null;
      } // End try block
      catch (Exception e) {
        throw new IlardiNetException("An error occurred while attempting Shutdown Blocking Socket Reader! System Message: " + e.getMessage(), e);
      }
    }
  }

  @Override
  public byte[] read() throws IlardiNetException {
    byte[] retBuf;
    int len;

    try {
      len = socketIns.read(readBuffer);

      synchronized (readerLock) {
        if (len >= 0) {
          retBuf = new byte[len];
          System.arraycopy(readBuffer, 0, retBuf, 0, len);
        }
        else {
          retBuf = null;
        }

        return retBuf;
      }
    } // End try block
    catch (Exception e) {
      throw new IlardiNetException("An error occurred while Blocking Socket Reader was Reading Socket! System Message: " + e.getMessage(), e);
    }
  }

  @Override
  public void interruptReader() throws IlardiNetException {
    try {
      if (socketIns != null) {
        socketIns.close();
      }
    }
    catch (Exception e) {
      System.err.println("Socket Input Stream Forced Closed to Interrupt Blocking Read Operation! Ignore Exception...");
      e.printStackTrace();
    }
  }

}
