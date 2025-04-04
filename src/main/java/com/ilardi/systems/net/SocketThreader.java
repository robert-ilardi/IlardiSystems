/**
 * Created Jan 16, 2021
 */
package com.ilardi.systems.net;

/**
 * @author rilardi
 *
 */

public interface SocketThreader {

  public void init() throws IlardiNetException;

  public void destroy() throws IlardiNetException;

  public void setSocketFramework(SocketFrameworkApi socketFramework);

  public void onConnect(SessionContext clientContext) throws IlardiNetException;

  public void onDisconnect(SessionContext clientContext) throws IlardiNetException;

  public void raiseSocketDisconnectSignal(SessionContext clientContext) throws IlardiNetException;

  public void raiseSocketDataSignal(SessionContext clientContext) throws IlardiNetException;

}
