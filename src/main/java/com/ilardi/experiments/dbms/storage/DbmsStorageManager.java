/**
 * Created Apr 1, 2021
 */
package com.ilardi.experiments.dbms.storage;

import com.ilardi.experiments.dbms.engine.DbmsBaseComponent;
import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author robert.ilardi
 *
 */

public abstract class DbmsStorageManager extends DbmsBaseComponent {

  private static final Logger logger = LogUtil.getInstance().getLogger(DbmsStorageManager.class);

  private final Object smLock;

  public DbmsStorageManager() {
    super();

    smLock = new Object();
  }

}
