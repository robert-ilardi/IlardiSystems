/**
 * Created Aug 10, 2016
 */
package com.ilardi.experiments.dbms.query.sql;

/**
 * @author rilardi
 *
 */

public class Operand extends BaseSqlElement {

  protected SqlColumn column;

  public Operand() {
    super();
  }

  public SqlColumn getColumn() {
    return column;
  }

  public void setColumn(SqlColumn column) {
    this.column = column;
  }

}
