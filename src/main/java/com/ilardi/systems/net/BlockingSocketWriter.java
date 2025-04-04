/**
 * Created Jan 9, 2021
 */
package com.ilardi.systems.net;

import java.io.OutputStream;
import java.net.Socket;

/**
 * @author rilardi
 *
 */

public class BlockingSocketWriter extends BaseSocketWriter {

  private Socket clientSocket;

  private OutputStream socketOuts;

  public BlockingSocketWriter() {
    super();
  }

  public void setClientSocket(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  @Override
  public void init() throws IlardiNetException {
    synchronized (writerLock) {
      System.out.println("BlockingSocketWriter Executing Init");

      try {
        socketOuts = clientSocket.getOutputStream();
      } // End try block
      catch (Exception e) {
        throw new IlardiNetException("An error occurred while attempting Initialize Blocking Socket Writer! System Message: " + e.getMessage(), e);
      }
    }
  }

  @Override
  public void close() throws IlardiNetException {
    synchronized (writerLock) {
      System.out.println("BlockingSocketWriter Executing Shutdown");

      try {
        socketOuts.close();
        socketOuts = null;

        clientSocket = null;
      } // End try block
      catch (Exception e) {
        throw new IlardiNetException("An error occurred while attempting Shutdown Blocking Socket Writer! System Message: " + e.getMessage(), e);
      }
    }
  }

  @Override
  public void write(byte[] data) throws IlardiNetException {
    synchronized (writerLock) {
      try {
        socketOuts.write(data);
      } // End try block
      catch (Exception e) {
        throw new IlardiNetException("An error occurred Blocking Socket Writer attempted to Write Data to Client Socket! System Message: " + e.getMessage(), e);
      }
    }
  }

}
