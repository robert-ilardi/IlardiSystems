/**
 * Created Jan 9, 2021
 */
package com.ilardi.systems.net.server;

import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;
import com.ilardi.systems.net.BaseSocketProcessor;
import com.ilardi.systems.net.IlardiNetException;
import com.ilardi.systems.net.SessionContext;

/**
 * @author rilardi
 *
 */

public class SimpleEchoSocketServerProcessor extends BaseSocketProcessor {

  protected static final Logger logger = LogUtil.getInstance().getLogger(SimpleEchoSocketServerProcessor.class);

  public SimpleEchoSocketServerProcessor() {
    super();
  }

  @Override
  public void init() throws IlardiNetException {
    logger.info("SimpleEchoServerSocketProcessor Executing Init");
  }

  @Override
  public void shutdown() throws IlardiNetException {
    logger.info("SimpleEchoServerSocketProcessor Executing Shutdown");
  }

  @Override
  public void process(SessionContext sessionContext) throws IlardiNetException {
    byte[] data;

    data = sessionContext.dequeueAllData();

    sessionContext.write(data);
  }

}
