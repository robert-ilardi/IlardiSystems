/**
 * Created Jan 16, 2021
 */
package com.ilardi.systems.net.client;

import com.ilardi.systems.net.IlardiNetException;

/**
 * @author rilardi
 *
 */

public class SocketClientJmx implements SocketClientJmxMBean {

  private SocketClient client;

  public SocketClientJmx() {}

  public void setSocketClient(SocketClient client) {
    this.client = client;
  }

  @Override
  public void destroy() throws IlardiNetException {
    client.destroy();
  }

}
