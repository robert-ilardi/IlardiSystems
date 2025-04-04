/**
 * Created Jan 16, 2021
 */
package com.ilardi.experiments.elasticj.core;

import com.ilardi.experiments.elasticj.util.EjException;

/**
 * @author rilardi
 *
 */

public class EjNodeJmx implements EjNodeJmxMBean {

  private EjNode node;

  public EjNodeJmx() {}

  public void setEjNode(EjNode node) {
    this.node = node;
  }

  @Override
  public void shutdown() throws EjException {
    node.shutdown();
  }

}
