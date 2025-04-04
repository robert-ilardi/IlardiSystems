/**
 * Created Apr 3, 2021
 */
package com.ilardi.experiments.dbms.query;

import java.io.Serializable;
import java.util.List;

/**
 * @author robert.ilardi
 *
 */

public class DbmsStatement implements Serializable {

  private String statementId;

  private String statementText;

  private List<DbmsStatementParameter> statementParameters;

  public DbmsStatement() {}

  public String getStatementId() {
    return statementId;
  }

  public void setStatementId(String statementId) {
    this.statementId = statementId;
  }

  public String getStatementText() {
    return statementText;
  }

  public void setStatementText(String statementText) {
    this.statementText = statementText;
  }

  public List<DbmsStatementParameter> getStatementParameters() {
    return statementParameters;
  }

  public void setStatementParameters(List<DbmsStatementParameter> statementParameters) {
    this.statementParameters = statementParameters;
  }

}
