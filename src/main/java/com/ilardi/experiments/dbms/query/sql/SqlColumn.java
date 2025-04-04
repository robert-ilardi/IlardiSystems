/**
 * Created Aug 10, 2016
 */
package com.ilardi.experiments.dbms.query.sql;

/**
 * @author rilardi
 *
 */

public class SqlColumn extends BaseSqlElement {

  protected String label;

  protected String tableAlias;

  protected SqlDataType dataType;

  protected Integer length;

  protected Integer precision;

  protected Boolean nullable;

  protected String defaultValue;

  protected Boolean unique;

  public SqlColumn() {
    super();
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getTableAlias() {
    return tableAlias;
  }

  public void setTableAlias(String tableAlias) {
    this.tableAlias = tableAlias;
  }

  public SqlDataType getDataType() {
    return dataType;
  }

  public void setDataType(SqlDataType dataType) {
    this.dataType = dataType;
  }

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public Integer getPrecision() {
    return precision;
  }

  public void setPrecision(Integer precision) {
    this.precision = precision;
  }

  public Boolean getNullable() {
    return nullable;
  }

  public void setNullable(Boolean nullable) {
    this.nullable = nullable;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public Boolean getUnique() {
    return unique;
  }

  public void setUnique(Boolean unique) {
    this.unique = unique;
  }

}
