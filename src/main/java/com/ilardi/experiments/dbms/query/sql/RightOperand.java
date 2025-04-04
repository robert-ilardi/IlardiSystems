/**
 * Created Aug 10, 2016
 */
package com.ilardi.experiments.dbms.query.sql;

import java.util.List;

/**
 * @author rilardi
 *
 */

public class RightOperand extends Operand {

  protected String value;

  protected Query subQuery;

  protected List<String> parameterList;

  protected SqlFunction function;

  public RightOperand() {
    super();
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Query getSubQuery() {
    return subQuery;
  }

  public void setSubQuery(Query subQuery) {
    this.subQuery = subQuery;
  }

  public List<String> getParameterList() {
    return parameterList;
  }

  public void setParameterList(List<String> parameterList) {
    this.parameterList = parameterList;
  }

  public SqlFunction getFunction() {
    return function;
  }

  public void setFunction(SqlFunction function) {
    this.function = function;
  }

}
