/**
 * Created Jan 16, 2021
 */
package com.ilardi.systems.net;

import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author rilardi
 *
 */

public abstract class BaseSocketHandler implements SocketHandler {

  protected static final Logger logger = LogUtil.getInstance().getLogger(BaseSocketHandler.class);

  protected SocketFrameworkApi socketFramework;

  protected final Object handlerLock;

  protected volatile boolean handlingSockets = false;
  protected volatile boolean runProcessing = false;

  public BaseSocketHandler() {
    handlerLock = new Object();
  }

  @Override
  public void setSocketFramework(SocketFrameworkApi socketFramework) {
    this.socketFramework = socketFramework;
  }

  @Override
  public void destroy() throws IlardiNetException {
    synchronized (handlerLock) {
      logger.info("BaseSocketHandler Executing destroy");

      socketFramework = null;

      handlerLock.notifyAll();
    }
  }

  @Override
  public boolean isHandlingSockets() {
    return handlingSockets;
  }

  @Override
  public void waitWhileIsHandlingSockets() throws IlardiNetException {
    synchronized (handlerLock) {
      logger.info("Waiting while Socket Framework is Handling Sockets");

      try {
        while (handlingSockets) {
          handlerLock.wait();
        }
      }
      catch (Exception e) {
        throw new IlardiNetException("An error occurred while waiting while Socket Framework is Handling Sockets. System Message: " + e.getMessage(), e);
      }
    }
  }

  @Override
  public void handleNewConnection(Object connectionObj) throws IlardiNetException {
    SocketSession clientSession;
    SessionContext clientContext;
    SocketReader clientReader;
    SocketWriter clientWriter;

    clientSession = socketFramework.createSocketSession(connectionObj);

    clientReader = createSocketReader(clientSession, connectionObj);

    clientWriter = createSocketWriter(clientSession, connectionObj);

    clientContext = socketFramework.createSessionContext(connectionObj, clientSession, clientReader, clientWriter);

    socketFramework.onConnect(clientContext);
  }

}
