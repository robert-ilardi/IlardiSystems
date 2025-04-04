/**
 * Created Aug 10, 2016
 */
package com.ilardi.experiments.dbms.query.sql;

import java.util.List;

/**
 * @author rilardi
 *
 */

public class SqlFunction extends SqlColumn {

  protected SqlFunction nestedFunction;

  protected SqlFunctionType type;

  protected List<SqlFunctionParameter> parameters;

  public SqlFunction() {
    super();
  }

  public SqlFunction getNestedFunction() {
    return nestedFunction;
  }

  public void setNestedFunction(SqlFunction nestedFunction) {
    this.nestedFunction = nestedFunction;
  }

  public SqlFunctionType getType() {
    return type;
  }

  public void setType(SqlFunctionType type) {
    this.type = type;
  }

  public List<SqlFunctionParameter> getParameters() {
    return parameters;
  }

  public void setParameters(List<SqlFunctionParameter> parameters) {
    this.parameters = parameters;
  }

}
