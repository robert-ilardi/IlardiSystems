/**
 * Created Aug 11, 2016
 */
package com.ilardi.experiments.dbms.query.sql;

/**
 * @author Robert
 *
 */

public class SqlForeignKey extends SqlConstraint {

  protected String columnName;

  protected String referencedTableName;

  protected String referencedColumnName;

  public SqlForeignKey() {
    super();
  }

  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  public String getReferencedTableName() {
    return referencedTableName;
  }

  public void setReferencedTableName(String referencedTableName) {
    this.referencedTableName = referencedTableName;
  }

  public String getReferencedColumnName() {
    return referencedColumnName;
  }

  public void setReferencedColumnName(String referencedColumnName) {
    this.referencedColumnName = referencedColumnName;
  }

}
