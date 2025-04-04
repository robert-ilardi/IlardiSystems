/**
 * Created Jan 23, 2021
 */
package com.ilardi.systems.net.client;

import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;
import com.ilardi.systems.net.BaseSocketProcessor;
import com.ilardi.systems.net.IlardiNetException;
import com.ilardi.systems.net.SessionContext;

/**
 * @author rilardi
 *
 */

public class SimpleEchoSocketClientProcessor extends BaseSocketProcessor {

  protected static final Logger logger = LogUtil.getInstance().getLogger(SimpleEchoSocketClientProcessor.class);

  public SimpleEchoSocketClientProcessor() {
    super();
  }

  @Override
  public void init() throws IlardiNetException {
    logger.info("SimpleEchoSocketClientProcessor Executing Init");
  }

  @Override
  public void shutdown() throws IlardiNetException {
    logger.info("SimpleEchoSocketClientProcessor Executing Shutdown");
  }

  @Override
  public void process(SessionContext sessionContext) throws IlardiNetException {
    byte[] data;
    String tmp;

    data = sessionContext.dequeueAllData();

    tmp = new String(data);

    System.out.println(tmp);
  }

}
