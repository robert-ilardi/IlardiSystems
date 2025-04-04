/**
 * Created Jan 16, 2021
 */
package com.ilardi.systems.net.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;
import com.ilardi.systems.net.BaseSocketFrameworkImpl;
import com.ilardi.systems.net.IlardiNetException;
import com.ilardi.systems.net.SessionContext;
import com.ilardi.systems.net.SocketReader;
import com.ilardi.systems.net.SocketSession;
import com.ilardi.systems.net.SocketWriter;

/**
 * @author rilardi
 *
 */

public abstract class BaseSocketServer extends BaseSocketFrameworkImpl {

  protected static final Logger logger = LogUtil.getInstance().getLogger(BaseSocketServer.class);

  protected HashMap<String, SessionContext> sessionContexts;

  public BaseSocketServer() {
    super();

    sessionContexts = new HashMap<String, SessionContext>();
  }

  public void closeAllSessions() throws IlardiNetException {
    Iterator<String> iter;
    ArrayList<String> keysCopy;
    String sessionId;
    SessionContext sessionCtx;

    synchronized (frameworkLock) {
      logger.info("Closing All Sessions");

      keysCopy = new ArrayList<String>();

      iter = sessionContexts.keySet().iterator();

      while (iter.hasNext()) {
        sessionId = iter.next();

        keysCopy.add(sessionId);
      }

      for (String key : keysCopy) {
        try {
          sessionCtx = sessionContexts.get(key);

          closeSession(sessionCtx);
        }
        catch (Exception e) {
          logger.error(e);
        }
      }
    }
  }

  @Override
  public SessionContext createSessionContext(Object connectionObj, SocketSession clientSession, SocketReader clientReader, SocketWriter clientWriter) throws IlardiNetException {
    synchronized (frameworkLock) {
      SessionContext sessionCtx = super.createSessionContext(connectionObj, clientSession, clientReader, clientWriter);

      String sessionId = sessionCtx.getSessionId();

      sessionContexts.put(sessionId, sessionCtx);

      return sessionCtx;
    }
  }

  @Override
  public void closeSession(SessionContext sessionCtx) throws IlardiNetException {
    synchronized (frameworkLock) {
      String sessionId = sessionCtx.getSessionId();

      super.closeSession(sessionCtx);

      sessionContexts.remove(sessionId);

      sessionId = null;
    }
  }

  @Override
  public void destroy() throws IlardiNetException {
    synchronized (frameworkLock) {
      logger.info("Destroying Server");

      super.destroy();

      try {
        if (sessionContexts != null) {
          closeAllSessions();

          sessionContexts.clear();
          sessionContexts = null;
        }
      }
      catch (Exception e) {
        logger.error(e);
      }
    } // End synchronized block
  }

  public void broadcast(byte[] data) throws IlardiNetException {
    Iterator<String> iter;
    String sessionId, tmp;
    SessionContext sessionCtx;

    synchronized (frameworkLock) {
      if (data != null && data.length > 0) {
        tmp = (new StringBuilder()).append("Broadcasting Data (Length = ").append(data.length).append(") to ALL Sessions").toString();
        logger.debug(tmp);

        iter = sessionContexts.keySet().iterator();

        while (iter.hasNext()) {
          sessionId = iter.next();

          try {
            sessionCtx = sessionContexts.get(sessionId);
            sessionCtx.write(data);
          }
          catch (Exception e) {
            logger.error(e);
          }
        }
      }
    }
  }

}
