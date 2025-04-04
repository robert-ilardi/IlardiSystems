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

public abstract class BaseSocketThreader implements SocketThreader {

  protected static final Logger logger = LogUtil.getInstance().getLogger(BaseSocketThreader.class);

  protected SocketFrameworkApi socketFramework;

  protected final Object threaderLock;

  public BaseSocketThreader() {
    threaderLock = new Object();
  }

  @Override
  public void setSocketFramework(SocketFrameworkApi socketFramework) {
    this.socketFramework = socketFramework;
  }

  @Override
  public void raiseSocketDisconnectSignal(SessionContext clientContext) throws IlardiNetException {
    socketFramework.raiseSocketDisconnectSignal(clientContext);
  }

  @Override
  public void raiseSocketDataSignal(SessionContext clientContext) throws IlardiNetException {
    socketFramework.raiseSocketDataSignal(clientContext);
  }

}
