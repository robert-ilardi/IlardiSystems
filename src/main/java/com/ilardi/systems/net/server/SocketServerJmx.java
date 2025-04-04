/**
 * Created Jan 16, 2021
 */
package com.ilardi.systems.net.server;

import com.ilardi.systems.net.IlardiNetException;

/**
 * @author rilardi
 *
 */

public class SocketServerJmx implements SocketServerJmxMBean {

  private SocketServer server;

  public SocketServerJmx() {}

  public void setSocketServer(SocketServer server) {
    this.server = server;
  }

  @Override
  public void destroy() throws IlardiNetException {
    server.destroy();
  }

}
