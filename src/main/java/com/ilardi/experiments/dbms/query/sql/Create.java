/**
 * Created Aug 10, 2016
 */
package com.ilardi.experiments.dbms.query.sql;

import java.util.List;

/**
 * @author Robert
 *
 */

public class Create extends Command {

  protected SqlTable table;

  protected List<SqlColumn> columns;

  protected SqlPrimaryKey primaryKey;

  protected List<SqlForeignKey> foreignKeys;

  public Create() {
    super();
  }

  public SqlTable getTable() {
    return table;
  }

  public void setTable(SqlTable table) {
    this.table = table;
  }

  public List<SqlColumn> getColumns() {
    return columns;
  }

  public void setColumns(List<SqlColumn> columns) {
    this.columns = columns;
  }

  public SqlPrimaryKey getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(SqlPrimaryKey primaryKey) {
    this.primaryKey = primaryKey;
  }

  public List<SqlForeignKey> getForeignKeys() {
    return foreignKeys;
  }

  public void setForeignKeys(List<SqlForeignKey> foreignKeys) {
    this.foreignKeys = foreignKeys;
  }

}
