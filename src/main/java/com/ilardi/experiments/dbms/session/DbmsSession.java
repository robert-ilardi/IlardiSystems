/**
 * Created Apr 3, 2021
 */
package com.ilardi.experiments.dbms.session;

import java.io.Serializable;
import java.util.List;

import com.ilardi.experiments.dbms.query.DbmsStatement;

/**
 * @author robert.ilardi
 *
 */

public class DbmsSession implements Serializable {

  private String sessionId;

  private List<DbmsStatement> activeStatements;

  public DbmsSession() {}

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public List<DbmsStatement> getActiveStatements() {
    return activeStatements;
  }

  public void setActiveStatements(List<DbmsStatement> activeStatements) {
    this.activeStatements = activeStatements;
  }

}
