/**
 * Created Jan 23, 2021
 */
package com.ilardi.systems.net.client;

import java.net.Socket;

import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;
import com.ilardi.systems.net.BaseSocketHandler;
import com.ilardi.systems.net.BlockingSocketReader;
import com.ilardi.systems.net.BlockingSocketWriter;
import com.ilardi.systems.net.IlardiNetException;
import com.ilardi.systems.net.SocketReader;
import com.ilardi.systems.net.SocketSession;
import com.ilardi.systems.net.SocketWriter;

/**
 * @author rilardi
 *
 */

public abstract class BlockingSocketClientHandler extends BaseSocketHandler {

  protected static final Logger logger = LogUtil.getInstance().getLogger(BlockingSocketClientHandler.class);

  protected Socket socket;

  public BlockingSocketClientHandler() {
    super();
  }

  @Override
  public void init() throws IlardiNetException {
    synchronized (handlerLock) {
      logger.info("BlockingSocketClientHandler Executing Init");

      try {
        if (socket == null) {
          socket = createSocket();
        }
      } // End try block
      catch (Exception e) {
        throw new IlardiNetException("An error occurred while attemping to Initialize Socket Client Handler! System Message: " + e.getMessage(), e);
      }
    }
  }

  protected abstract Socket createSocket() throws IlardiNetException;

  protected abstract void connectSocket() throws IlardiNetException;

  @Override
  public void startSocketHandling() throws IlardiNetException {
    synchronized (handlerLock) {
      try {
        logger.info("BlockingSocketClientHandler Executing startSocketHandling");

        runProcessing = true;
        handlingSockets = true;

        connectSocket();

        handleNewConnection(socket);
      } // End try block
      catch (Exception e) {
        throw new IlardiNetException("An error occurred while attempting to Start Server Socket Handling! System Message: " + e.getMessage(), e);
      }
    }
  }

  @Override
  public void stopSocketHandling() throws IlardiNetException {
    synchronized (handlerLock) {
      try {
        logger.info("BlockingSocketClientHandler Executing stopSocketHandling");

        runProcessing = false;

        socket.close();

        handlingSockets = false;
        handlerLock.notifyAll();
      } // End try block
      catch (Exception e) {
        throw new IlardiNetException("An error occurred while attempting to Stop Server Socket Handling! System Message: " + e.getMessage(), e);
      }
    }
  }

  @Override
  public void destroy() throws IlardiNetException {
    synchronized (handlerLock) {
      logger.info("BlockingSocketClientHandler Executing destroy");

      stopSocketHandling();
      socket = null;

      super.destroy();
    }
  }

  @Override
  public SocketReader createSocketReader(SocketSession clientSession, Object connectionObj) throws IlardiNetException {
    Socket clientSocket;
    BlockingSocketReader socketReader;

    clientSocket = (Socket) connectionObj;

    socketReader = new BlockingSocketReader();
    socketReader.setClientSocket(clientSocket);

    return socketReader;
  }

  @Override
  public SocketWriter createSocketWriter(SocketSession clientSession, Object connectionObj) throws IlardiNetException {
    Socket clientSocket;
    BlockingSocketWriter socketWriter;

    clientSocket = (Socket) connectionObj;

    socketWriter = new BlockingSocketWriter();
    socketWriter.setClientSocket(clientSocket);

    return socketWriter;
  }

}
