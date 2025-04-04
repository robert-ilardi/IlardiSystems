/**
 * Created Mar 28, 2021
 */
package com.ilardi.experiments.dbms;

/**
 * @author rilardi
 *
 */

public class AppDbJmx implements AppDbJmxMBean {

  private AppDb appDb;

  public AppDbJmx() {}

  public void setAppDb(AppDb appDb) {
    this.appDb = appDb;
  }

  @Override
  public void shutdown() throws AppDbException {
    appDb.shutdown();
  }

}
