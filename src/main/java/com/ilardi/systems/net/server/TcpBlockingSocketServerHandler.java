/**
 * Created Jan 8, 2021
 */
package com.ilardi.systems.net.server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;
import com.ilardi.systems.net.IlardiNetException;
import com.ilardi.systems.net.SocketInfo;

/**
 * @author rilardi
 *
 */

public class TcpBlockingSocketServerHandler extends BlockingSocketServerHandler {

  protected static final Logger logger = LogUtil.getInstance().getLogger(TcpBlockingSocketServerHandler.class);

  public TcpBlockingSocketServerHandler() {
    super();
  }

  @Override
  protected ServerSocket createServerSocket() throws IlardiNetException {
    ServerSocket ss;

    logger.info("TcpBlockingSocketServerHandler Executing createServerSocket");

    try {
      ss = new ServerSocket();

      return ss;
    } // End try block
    catch (Exception e) {
      throw new IlardiNetException("An error occurred while attemping to Create Server Socket! System Message: " + e.getMessage(), e);
    }
  }

  @Override
  protected void initServerSocket() throws IlardiNetException {
    InetSocketAddress sockAddr;
    SocketInfo socketInfo;

    logger.info("TcpBlockingSocketServerHandler Executing initServerSocket");

    try {
      socketInfo = socketFramework.getSocketInfo();

      if (socketInfo.getHostAddress() != null) {
        sockAddr = new InetSocketAddress(socketInfo.getHostAddress(), socketInfo.getPort());
      }
      else {
        sockAddr = new InetSocketAddress(socketInfo.getPort());
      }

      serverSocket.bind(sockAddr);
    } // End try block
    catch (Exception e) {
      throw new IlardiNetException("An error occurred while attemping to Initialize Server Socket! System Message: " + e.getMessage(), e);
    }
  }

}
