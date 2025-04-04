/**
 * Created Aug 28, 2021
 */
package com.ilardi.experiments.dbms.mediator;

import com.ilardi.experiments.dbms.endpoint.DbmsEndpointManager;
import com.ilardi.experiments.dbms.engine.DbmsBaseComponent;
import com.ilardi.experiments.dbms.engine.DbmsEngine;
import com.ilardi.experiments.dbms.query.DbmsQueryManager;
import com.ilardi.experiments.dbms.security.DbmsSecurityManager;
import com.ilardi.experiments.dbms.session.DbmsSessionManager;
import com.ilardi.experiments.dbms.storage.DbmsStorageManager;
import com.ilardi.experiments.dbms.transport.DbmsTransportManager;
import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author robert.ilardi
 *
 */

public abstract class DbmsMediator extends DbmsBaseComponent {

  private static final Logger logger = LogUtil.getInstance().getLogger(DbmsMediator.class);

  private final Object mediatorLock;

  protected DbmsEngine dbmsEngine;

  protected DbmsSecurityManager securityMan;

  protected DbmsStorageManager storageMan;

  protected DbmsSessionManager sessionMan;

  protected DbmsQueryManager queryMan;

  protected DbmsTransportManager transportMan;

  protected DbmsEndpointManager endpointMan;

  public DbmsMediator() {
    super();

    mediatorLock = new Object();
  }

  public void setDbmsEngine(DbmsEngine dbmsEngine) {
    this.dbmsEngine = dbmsEngine;
  }

  public void setSecurityMan(DbmsSecurityManager securityMan) {
    this.securityMan = securityMan;
  }

  public void setStorageMan(DbmsStorageManager storageMan) {
    this.storageMan = storageMan;
  }

  public void setSessionMan(DbmsSessionManager sessionMan) {
    this.sessionMan = sessionMan;
  }

  public void setQueryMan(DbmsQueryManager queryMan) {
    this.queryMan = queryMan;
  }

  public void setTransportMan(DbmsTransportManager transportMan) {
    this.transportMan = transportMan;
  }

  public void setEndpointMan(DbmsEndpointManager endpointMan) {
    this.endpointMan = endpointMan;
  }

}
