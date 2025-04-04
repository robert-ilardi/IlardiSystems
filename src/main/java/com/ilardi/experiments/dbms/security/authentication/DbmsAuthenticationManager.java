/**
 * Created Aug 25, 2021
 */
package com.ilardi.experiments.dbms.security.authentication;

import com.ilardi.experiments.dbms.engine.DbmsBaseComponent;
import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author robert.ilardi
 *
 */

public abstract class DbmsAuthenticationManager extends DbmsBaseComponent {

  private static final Logger logger = LogUtil.getInstance().getLogger(DbmsAuthenticationManager.class);

  private final Object amLock;

  public DbmsAuthenticationManager() {
    super();

    amLock = new Object();
  }

}
