/**
 * Created Jan 16, 2021
 */
package com.ilardi.systems.net;

/**
 * @author rilardi
 *
 */

public interface SocketFrameworkController {

  public void init() throws IlardiNetException;

  public void destroy() throws IlardiNetException;

  public void onConnect(SessionContext clientContext) throws IlardiNetException;

  public void onDisconnect(SessionContext clientContext) throws IlardiNetException;

  public void onData(SessionContext clientContext) throws IlardiNetException;

}
