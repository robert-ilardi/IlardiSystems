/**
 * Created Jan 15, 2021
 */
package com.ilardi.systems.net;

/**
 * @author rilardi
 *
 */

public interface SocketHandler {

  public void init() throws IlardiNetException;

  public void destroy() throws IlardiNetException;

  public void setSocketFramework(SocketFrameworkApi socketFramework);

  public boolean isHandlingSockets();

  public void waitWhileIsHandlingSockets() throws IlardiNetException;

  public void handleNewConnection(Object connectionObj) throws IlardiNetException;

  public SocketReader createSocketReader(SocketSession clientSession, Object connectionObj) throws IlardiNetException;

  public SocketWriter createSocketWriter(SocketSession clientSession, Object connectionObj) throws IlardiNetException;

  public void startSocketHandling() throws IlardiNetException;

  public void stopSocketHandling() throws IlardiNetException;

}
