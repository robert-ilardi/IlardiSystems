/**
 * Created Apr 1, 2021
 */
package com.ilardi.experiments.dbms.endpoint;

import com.ilardi.experiments.dbms.engine.DbmsBaseComponent;
import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author robert.ilardi
 *
 */

public abstract class DbmsEndpointManager extends DbmsBaseComponent {

  private static final Logger logger = LogUtil.getInstance().getLogger(DbmsEndpointManager.class);

  private final Object epmLock;

  public DbmsEndpointManager() {
    super();

    epmLock = new Object();
  }

}
