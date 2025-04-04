/**
 * Created Aug 10, 2016
 */
package com.ilardi.experiments.dbms.query.sql;

import java.io.Serializable;

/**
 * @author rilardi
 *
 */

public abstract class BaseSqlElement implements Serializable {

  protected int index;

  protected String name;

  public BaseSqlElement() {}

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
