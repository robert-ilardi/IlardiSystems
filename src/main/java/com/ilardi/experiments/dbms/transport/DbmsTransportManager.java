/**
 * Created Apr 3, 2021
 */
package com.ilardi.experiments.dbms.transport;

import com.ilardi.experiments.dbms.engine.DbmsBaseComponent;
import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author robert.ilardi
 *
 */

public abstract class DbmsTransportManager extends DbmsBaseComponent {

  private static final Logger logger = LogUtil.getInstance().getLogger(DbmsTransportManager.class);

  private final Object tmLock;

  public DbmsTransportManager() {
    super();

    tmLock = new Object();
  }

}
