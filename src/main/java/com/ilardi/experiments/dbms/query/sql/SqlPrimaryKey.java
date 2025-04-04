/**
 * Created Aug 11, 2016
 */
package com.ilardi.experiments.dbms.query.sql;

import java.util.List;

/**
 * @author Robert
 *
 */

public class SqlPrimaryKey extends SqlConstraint {

  protected List<Integer> keyOrdinals;

  public SqlPrimaryKey() {
    super();
  }

  public List<Integer> getKeyOrdinals() {
    return keyOrdinals;
  }

  public void setKeyOrdinals(List<Integer> keyOrdinals) {
    this.keyOrdinals = keyOrdinals;
  }

}
