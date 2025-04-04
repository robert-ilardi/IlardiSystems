/**
 * Created Apr 4, 2021
 */
package com.ilardi.experiments.dbms.query;

import java.io.Serializable;

/**
 * @author robert.ilardi
 *
 */

public class DbmsStatementParameter implements Serializable {

  private Integer index;
  private String name;
  private Object value;
  private DbmsDataType dataType;

  public DbmsStatementParameter() {}

  public Integer getIndex() {
    return index;
  }

  public void setIndex(Integer index) {
    this.index = index;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public DbmsDataType getDataType() {
    return dataType;
  }

  public void setDataType(DbmsDataType dataType) {
    this.dataType = dataType;
  }

}
