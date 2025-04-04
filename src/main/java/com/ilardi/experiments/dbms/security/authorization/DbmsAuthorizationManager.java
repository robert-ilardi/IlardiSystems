/**
 * Created Aug 25, 2021
 */
package com.ilardi.experiments.dbms.security.authorization;

import com.ilardi.experiments.dbms.engine.DbmsBaseComponent;
import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author robert.ilardi
 *
 */

public abstract class DbmsAuthorizationManager extends DbmsBaseComponent {

  private static final Logger logger = LogUtil.getInstance().getLogger(DbmsAuthorizationManager.class);

  private final Object amLock;

  public DbmsAuthorizationManager() {
    super();

    amLock = new Object();
  }

}
