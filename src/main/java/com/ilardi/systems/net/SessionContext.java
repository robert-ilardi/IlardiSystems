/**
 * Created Jan 16, 2021
 */
package com.ilardi.systems.net;

/**
 * @author rilardi
 *
 */

public class SessionContext {

  private String sessionId;

  private SocketInfo socketInfo;

  private SocketSession clientSession;

  private SocketReader clientReader;

  private SocketWriter clientWriter;

  private final Object contextLock;

  public SessionContext(String sessionId, SocketInfo socketInfo, SocketSession clientSession, SocketReader clientReader, SocketWriter clientWriter) {
    contextLock = new Object();

    this.sessionId = sessionId;
    this.socketInfo = socketInfo;

    this.clientSession = clientSession;
    this.clientReader = clientReader;
    this.clientWriter = clientWriter;
  }

  public String getSessionId() {
    return sessionId;
  }

  public SocketInfo getSocketInfo() {
    return socketInfo;
  }

  public SocketSession getClientSession() {
    return clientSession;
  }

  public SocketWriter getClientWriter() {
    return clientWriter;
  }

  public SocketReader getClientReader() {
    return clientReader;
  }

  public void init() throws IlardiNetException {
    synchronized (contextLock) {
      System.out.println((new StringBuilder()).append("Initializing Session Context for Session Id = ").append(sessionId).toString());

      clientWriter.init();
      clientReader.init();
    }
  }

  public void close() throws IlardiNetException {
    synchronized (contextLock) {
      try {
        if (clientReader != null) {
          clientReader.close();
          clientReader = null;
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }

      try {
        if (clientWriter != null) {
          clientWriter.close();
          clientWriter = null;
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }

      try {
        if (clientSession != null) {
          clientSession.destroy();
          clientSession = null;
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }

      sessionId = null;
      socketInfo = null;
    }
  }

  public byte[] dequeueAllData() {
    return clientSession.dequeueAllData();
  }

  public byte[] dequeueData() {
    return clientSession.dequeueData();
  }

  public void write(byte[] data) throws IlardiNetException {
    clientWriter.write(data);
  }

  public byte[] read() throws IlardiNetException {
    return clientReader.read();
  }

  public void enqueueData(byte[] data) {
    clientSession.enqueueData(data);
  }

  public void enqueueData(byte data) {
    clientSession.enqueueData(new byte[] { data });
  }

  public void interruptReader() throws IlardiNetException {
    clientReader.interruptReader();
  }

  public void pushData(byte[] data) {
    clientSession.pushData(data);
  }

  public byte[] dequeueData(int len) {
    return clientSession.dequeueData(len);
  }

}
