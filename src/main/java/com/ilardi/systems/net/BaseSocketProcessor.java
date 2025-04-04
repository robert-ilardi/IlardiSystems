/**
 * Created Jan 9, 2021
 */
package com.ilardi.systems.net;

import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author rilardi
 *
 */

public abstract class BaseSocketProcessor implements SocketProcessor {

  protected static final Logger logger = LogUtil.getInstance().getLogger(BaseSocketProcessor.class);

  protected final Object processorLock;

  protected SocketThreader threader;

  public BaseSocketProcessor() {
    processorLock = new Object();
  }

  @Override
  public void setSocketThreader(SocketThreader threader) {
    this.threader = threader;
  }

}
