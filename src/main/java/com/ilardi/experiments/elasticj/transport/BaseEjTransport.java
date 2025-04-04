/**
 * Created Jan 31, 2021
 */
package com.ilardi.experiments.elasticj.transport;

import com.ilardi.experiments.elasticj.model.EjNodeInfo;
import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author rilardi
 *
 */

public abstract class BaseEjTransport implements EjTransport {

  protected static final Logger logger = LogUtil.getInstance().getLogger(BaseEjTransport.class);

  protected final Object transportLock;

  protected EjNodeInfo nodeInfo;

  public BaseEjTransport() {
    transportLock = new Object();
  }

  @Override
  public void setNodeInfo(EjNodeInfo nodeInfo) {
    this.nodeInfo = nodeInfo;
  }

}
