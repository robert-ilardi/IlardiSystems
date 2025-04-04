/**
 * Created Aug 10, 2016
 */
package com.ilardi.experiments.dbms.query.sql;

/**
 * @author rilardi
 *
 */

public class SqlFunctionParameter extends BaseSqlElement {

  // protected Column column;

  protected String value;
  protected SqlDataType valueType;

  public SqlFunctionParameter() {
    super();
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public SqlDataType getValueType() {
    return valueType;
  }

  public void setValueType(SqlDataType valueType) {
    this.valueType = valueType;
  }

}
