/**
 * Created Jan 8, 2021
 */
package com.ilardi.systems.net.server;

import java.net.ServerSocket;
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

public abstract class BlockingSocketServerHandler extends BaseSocketHandler {

  protected static final Logger logger = LogUtil.getInstance().getLogger(BaseSocketServer.class);

  protected ServerSocket serverSocket;

  private Thread serverThread;

  public BlockingSocketServerHandler() {
    super();
  }

  @Override
  public void init() throws IlardiNetException {
    synchronized (handlerLock) {
      logger.info("BlockingSocketServerHandler Executing Init");

      try {
        if (serverSocket == null) {
          serverSocket = createServerSocket();

          initServerSocket();
        }
      } // End try block
      catch (Exception e) {
        throw new IlardiNetException("An error occurred while attemping to Initialize Socket Server Handler! System Message: " + e.getMessage(), e);
      }
    }
  }

  protected abstract ServerSocket createServerSocket() throws IlardiNetException;

  protected abstract void initServerSocket() throws IlardiNetException;

  @Override
  public void startSocketHandling() throws IlardiNetException {
    synchronized (handlerLock) {
      try {
        logger.info("BlockingSocketServerHandler Executing startSocketHandling");

        runProcessing = true;

        serverThread = new Thread(serverRunner);
        serverThread.start();

        while (!handlingSockets) {
          handlerLock.wait();
        }
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
        logger.info("BlockingSocketServerHandler Executing stopSocketHandling");

        runProcessing = false;

        serverSocket.close();

        while (handlingSockets) {
          handlerLock.wait();
        }
      } // End try block
      catch (Exception e) {
        throw new IlardiNetException("An error occurred while attempting to Stop Server Socket Handling! System Message: " + e.getMessage(), e);
      }
    }
  }

  @Override
  public void destroy() throws IlardiNetException {
    synchronized (handlerLock) {
      logger.info("BlockingSocketServerHandler Executing destroy");

      stopSocketHandling();
      serverSocket = null;

      super.destroy();
    }
  }

  private Runnable serverRunner = new Runnable() {
    @Override
    public void run() {
      try {
        synchronized (handlerLock) {
          handlingSockets = true;
          handlerLock.notifyAll();
        }

        serverProcessingLoop();
      } // End try block
      catch (Exception e) {
        logger.error(e);
      }
      finally {
        synchronized (handlerLock) {
          handlingSockets = false;
          handlerLock.notifyAll();
        }
      }
    }
  };

  protected void serverProcessingLoop() throws IlardiNetException {
    Socket clientSocket;

    logger.info("BlockingSocketServerHandler Entering Server Processing Loop");

    try {
      while (runProcessing) {
        clientSocket = serverSocket.accept();

        if (runProcessing) {
          synchronized (handlerLock) {
            try {
              if (runProcessing) {
                handleNewConnection(clientSocket);
              }
              else {
                try {
                  clientSocket.close();
                  clientSocket = null;
                }
                catch (Exception e) {
                  logger.error(e);
                }
              } // End else block
            } // End try block
            catch (Exception e) {
              logger.error(e);

              if (clientSocket != null) {
                try {
                  clientSocket.close();
                  clientSocket = null;
                }
                catch (Exception e2) {
                  logger.error(e);
                }
              }
            } // End catch block
          } // End synchronized (handlerLock)
        } // End if (runProcessing) block

        clientSocket = null;
      } // End while (runProcessing)
    } // End try block
    catch (Exception e) {
      throw new IlardiNetException("An error occurred while Server Processing was Executing! System Message: " + e.getMessage(), e);
    }

    logger.info("BlockingSocketServerHandler Exiting Server Processing Loop");
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
