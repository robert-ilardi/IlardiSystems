/**
 * Created Apr 3, 2021
 */
package com.ilardi.experiments.dbms.engine;

import com.ilardi.experiments.dbms.AppDbException;
import com.ilardi.experiments.dbms.mediator.DbmsMediator;
import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author robert.ilardi
 *
 */

public abstract class DbmsBaseComponent {

  private static final Logger logger = LogUtil.getInstance().getLogger(DbmsBaseComponent.class);

  private final Object bcLock;

  protected DbmsInfo dbmsInfo;

  protected DbmsMediator dbmsMediator;

  public DbmsBaseComponent() {
    bcLock = new Object();
  }

  public void setDbmsInfo(DbmsInfo dbmsInfo) {
    this.dbmsInfo = dbmsInfo;
  }

  public void setDbmsMediator(DbmsMediator dbmsMediator) {
    this.dbmsMediator = dbmsMediator;
  }

  public abstract void initDbmsComponent() throws AppDbException;

  public abstract void shutdownDbmsComponent() throws AppDbException;

}
