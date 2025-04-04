/**
 * Created Aug 25, 2021
 */
package com.ilardi.experiments.dbms.security;

import com.ilardi.experiments.dbms.engine.DbmsBaseComponent;
import com.ilardi.experiments.dbms.security.authentication.DbmsAuthenticationManager;
import com.ilardi.experiments.dbms.security.authorization.DbmsAuthorizationManager;
import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author robert.ilardi
 *
 */

public abstract class DbmsSecurityManager extends DbmsBaseComponent {

  private static final Logger logger = LogUtil.getInstance().getLogger(DbmsSecurityManager.class);

  private final Object amLock;

  protected DbmsAuthenticationManager authenticationMan;

  protected DbmsAuthorizationManager authorizationMan;

  public DbmsSecurityManager() {
    super();

    amLock = new Object();
  }

  public void setAuthenticationManager(DbmsAuthenticationManager authenticationMan) {
    this.authenticationMan = authenticationMan;
  }

  public void setAuthorizationManager(DbmsAuthorizationManager authorizationMan) {
    this.authorizationMan = authorizationMan;
  }

}
