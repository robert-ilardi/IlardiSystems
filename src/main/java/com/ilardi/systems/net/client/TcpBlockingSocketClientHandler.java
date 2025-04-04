/**
 * Created Jan 8, 2021
 */
package com.ilardi.systems.net.client;

import java.net.InetSocketAddress;
import java.net.Socket;

import com.ilardi.systems.net.IlardiNetException;
import com.ilardi.systems.net.SocketInfo;

/**
 * @author rilardi
 *
 */

public class TcpBlockingSocketClientHandler extends BlockingSocketClientHandler {

  public TcpBlockingSocketClientHandler() {
    super();
  }

  @Override
  protected Socket createSocket() throws IlardiNetException {
    Socket s;

    System.out.println("TcpBlockingSocketClientHandler Executing createSocket");

    try {
      s = new Socket();

      return s;
    } // End try block
    catch (Exception e) {
      throw new IlardiNetException("An error occurred while attemping to Create Socket! System Message: " + e.getMessage(), e);
    }
  }

  @Override
  protected void connectSocket() throws IlardiNetException {
    InetSocketAddress sockAddr;
    SocketInfo socketInfo;

    System.out.println("TcpBlockingSocketClientHandler Executing connectSocket");

    try {
      socketInfo = socketFramework.getSocketInfo();

      if (socketInfo.getHostAddress() != null) {
        sockAddr = new InetSocketAddress(socketInfo.getHostAddress(), socketInfo.getPort());
      }
      else {
        sockAddr = new InetSocketAddress(socketInfo.getPort());
      }

      socket.connect(sockAddr);
    } // End try block
    catch (Exception e) {
      throw new IlardiNetException("An error occurred while attemping to Connect Socket! System Message: " + e.getMessage(), e);
    }
  }

}
