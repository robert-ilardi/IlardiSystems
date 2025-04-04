/**
 * Created Jan 24, 2021
 */
package com.ilardi.systems.net.intalk;

import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;
import com.ilardi.systems.net.BaseSocketFrameworkController;
import com.ilardi.systems.net.IlardiNetException;
import com.ilardi.systems.net.SessionContext;

/**
 * @author rilardi
 *
 */

public abstract class InTalkSocketFrameworkController extends BaseSocketFrameworkController {

  protected static final Logger logger = LogUtil.getInstance().getLogger(InTalkSocketFrameworkController.class);

  public InTalkSocketFrameworkController() {
    super();
  }

  @Override
  public void init() throws IlardiNetException {
    logger.info("InTalkSocketFrameworkController Executing init");
  }

  @Override
  public void destroy() throws IlardiNetException {
    logger.info("InTalkSocketFrameworkController Executing destroy");
  }

  @Override
  public void onConnect(SessionContext clientContext) throws IlardiNetException {
    logger.info("InTalkSocketFrameworkController Executing onConnect");
  }

  @Override
  public void onDisconnect(SessionContext clientContext) throws IlardiNetException {
    logger.info("InTalkSocketFrameworkController Executing onDisconnect");
  }

  @Override
  public void onData(SessionContext clientContext) throws IlardiNetException {
    logger.debug("InTalkSocketFrameworkController Executing onData");
  }

}
