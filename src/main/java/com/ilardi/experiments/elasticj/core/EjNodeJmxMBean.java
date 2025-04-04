/**
 * Created Jan 16, 2021
 */
package com.ilardi.experiments.elasticj.core;

import com.ilardi.experiments.elasticj.util.EjException;

/**
 * @author rilardi
 *
 */

public interface EjNodeJmxMBean {

  public void shutdown() throws EjException;

}
