/**
 * Created Aug 10, 2016
 */
package com.ilardi.experiments.dbms.query.sql;

/**
 * @author Robert
 *
 */

public class Command extends BaseSqlElement {

  protected String sqlText;

  protected SqlStatement statement;

  public Command() {
    super();
  }

  public String getSqlText() {
    return sqlText;
  }

  public void setSqlText(String sqlText) {
    this.sqlText = sqlText;
  }

  public SqlStatement getStatement() {
    return statement;
  }

  public void setStatement(SqlStatement statement) {
    this.statement = statement;
  }

}
