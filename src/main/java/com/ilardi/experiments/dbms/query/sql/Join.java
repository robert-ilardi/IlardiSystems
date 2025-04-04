/**
 * Created Aug 10, 2016
 */
package com.ilardi.experiments.dbms.query.sql;

import java.util.List;

/**
 * @author rilardi
 *
 */

public class Join extends BaseSqlElement {

  protected SqlTable table;

  protected JoinType type;

  protected List<JoinOnParameter> joinOnParameters;

  public Join() {
    super();
  }

  public SqlTable getTable() {
    return table;
  }

  public void setTable(SqlTable table) {
    this.table = table;
  }

  public JoinType getType() {
    return type;
  }

  public void setType(JoinType type) {
    this.type = type;
  }

  public List<JoinOnParameter> getJoinOnParameters() {
    return joinOnParameters;
  }

  public void setJoinOnParameters(List<JoinOnParameter> joinOnParameters) {
    this.joinOnParameters = joinOnParameters;
  }

}
