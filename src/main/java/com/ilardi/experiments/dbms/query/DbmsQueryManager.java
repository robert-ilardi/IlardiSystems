/**
 * Created Apr 1, 2021
 */
package com.ilardi.experiments.dbms.query;

import com.ilardi.experiments.dbms.engine.DbmsBaseComponent;
import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author robert.ilardi
 *
 */

public abstract class DbmsQueryManager extends DbmsBaseComponent {

  private static final Logger logger = LogUtil.getInstance().getLogger(DbmsQueryManager.class);

  private final Object qmLock;

  public DbmsQueryManager() {
    super();

    qmLock = new Object();
  }

}
