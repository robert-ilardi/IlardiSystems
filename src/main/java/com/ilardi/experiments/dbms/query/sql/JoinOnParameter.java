/**
 * Created Aug 10, 2016
 */
package com.ilardi.experiments.dbms.query.sql;

/**
 * @author rilardi
 *
 */

public class JoinOnParameter extends BaseSqlElement {

  protected SqlTable leftTable;
  protected SqlTable rightTable;

  protected SqlColumn leftColumn;
  protected SqlColumn rightColumn;

  public JoinOnParameter() {
    super();
  }

  public SqlTable getLeftTable() {
    return leftTable;
  }

  public void setLeftTable(SqlTable leftTable) {
    this.leftTable = leftTable;
  }

  public SqlTable getRightTable() {
    return rightTable;
  }

  public void setRightTable(SqlTable rightTable) {
    this.rightTable = rightTable;
  }

  public SqlColumn getLeftColumn() {
    return leftColumn;
  }

  public void setLeftColumn(SqlColumn leftColumn) {
    this.leftColumn = leftColumn;
  }

  public SqlColumn getRightColumn() {
    return rightColumn;
  }

  public void setRightColumn(SqlColumn rightColumn) {
    this.rightColumn = rightColumn;
  }

}
