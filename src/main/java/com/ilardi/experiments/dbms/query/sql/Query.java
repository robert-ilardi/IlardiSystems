/**
 * Created Aug 10, 2016
 */
package com.ilardi.experiments.dbms.query.sql;

import java.util.List;

/**
 * @author rilardi
 *
 */

public class Query extends Command {

  protected boolean uniqueEnabled;

  protected List<SqlTable> tables;

  protected List<Join> joins;

  protected List<SqlColumn> columns;

  public Query() {
    super();
  }

  public boolean isUniqueEnabled() {
    return uniqueEnabled;
  }

  public void setUniqueEnabled(boolean uniqueEnabled) {
    this.uniqueEnabled = uniqueEnabled;
  }

  public List<SqlTable> getTables() {
    return tables;
  }

  public void setTables(List<SqlTable> tables) {
    this.tables = tables;
  }

  public List<Join> getJoins() {
    return joins;
  }

  public void setJoins(List<Join> joins) {
    this.joins = joins;
  }

  public List<SqlColumn> getColumns() {
    return columns;
  }

  public void setColumns(List<SqlColumn> columns) {
    this.columns = columns;
  }

}
