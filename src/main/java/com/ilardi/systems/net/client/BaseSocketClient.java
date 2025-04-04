/**
 * Created Jan 18, 2021
 */
package com.ilardi.systems.net.client;

import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;
import com.ilardi.systems.net.BaseSocketFrameworkImpl;
import com.ilardi.systems.net.IlardiNetException;
import com.ilardi.systems.net.SessionContext;

/**
 * @author rilardi
 *
 */

public abstract class BaseSocketClient extends BaseSocketFrameworkImpl {

  protected static final Logger logger = LogUtil.getInstance().getLogger(BaseSocketClient.class);

  protected SessionContext sessionContext;

  public BaseSocketClient() {
    super();
  }

  @Override
  public void closeSession(SessionContext sessionCtx) throws IlardiNetException {
    synchronized (frameworkLock) {
      String sessionId = sessionCtx.getSessionId();

      String tmp = (new StringBuilder()).append("Closing Session = ").append(sessionId).toString();
      logger.info(tmp);

      socketHandler.stopSocketHandling();

      super.closeSession(sessionCtx);
      sessionContext = null;
    }
  }

  @Override
  public void destroy() throws IlardiNetException {
    synchronized (frameworkLock) {
      logger.info("Destroying Client");

      super.destroy();

      if (sessionContext != null) {
        closeSession(sessionContext);
      }
    } // End synchronized block
  }

}
