/**
 * Created Apr 3, 2021
 */
package com.ilardi.experiments.dbms.session;

import com.ilardi.experiments.dbms.engine.DbmsBaseComponent;
import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author robert.ilardi
 *
 */

public abstract class DbmsSessionManager extends DbmsBaseComponent {

  private static final Logger logger = LogUtil.getInstance().getLogger(DbmsSessionManager.class);

  private final Object smLock;

  public DbmsSessionManager() {
    super();

    smLock = new Object();
  }

}
