/**
 * Created Jan 18, 2021
 */
package com.ilardi.systems.net;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author rilardi
 *
 */

public interface SocketFrameworkApi {

  public void loadSocketFrameworkProperties(String frameworkPropsFilePath) throws IlardiNetException;

  public void loadSocketFrameworkProperties(InputStream ins) throws IlardiNetException;

  public void setSocketFrameworkProperties(Properties frameworkProps);

  public void init() throws IlardiNetException;

  public void startSocketHandling() throws IlardiNetException;

  public void stopHandlingSockets() throws IlardiNetException;

  public void destroy() throws IlardiNetException;

  public SocketInfo getSocketInfo();

  public boolean isHandlingSockets();

  public void waitWhileIsHandlingSockets() throws IlardiNetException;

  public SocketInfo createSocketInfo() throws IlardiNetException;

  public void onConnect(SessionContext clientContext) throws IlardiNetException;

  public void onDisconnect(SessionContext clientContext) throws IlardiNetException;

  public void raiseSocketDataSignal(SessionContext clientContext) throws IlardiNetException;

  public void raiseSocketDisconnectSignal(SessionContext clientContext) throws IlardiNetException;

  public SessionContext createSessionContext(Object connectionObj, SocketSession clientSession, SocketReader clientReader, SocketWriter clientWriter) throws IlardiNetException;

  public SocketSession createSocketSession(Object connectionObj) throws IlardiNetException;

  public String generateSessionId(Object connectionObj) throws IlardiNetException;

  public void closeSession(SessionContext clientContext) throws IlardiNetException;

  public SocketFrameworkController createSocketFrameworkController() throws IlardiNetException;

  public SocketProcessor createSocketProcessor() throws IlardiNetException;

  public SocketThreader createSocketThreader() throws IlardiNetException;

  public SocketHandler createSocketHandler() throws IlardiNetException;

}
