/**
 * Created Jun 20, 2017
 */
package com.ilardi.experiments.etl;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

/**
 * @author rilardi
 *
 */

@XmlType(name = "RawDataElement")
public class RawDataElement implements Serializable {

  protected boolean arrayElement;

  protected int arrayIndex;

  protected String name;

  protected String dataType;

  protected Object value;

  public RawDataElement() {}

  public boolean isArrayElement() {
    return arrayElement;
  }

  public void setArrayElement(boolean arrayElement) {
    this.arrayElement = arrayElement;
  }

  public int getArrayIndex() {
    return arrayIndex;
  }

  public void setArrayIndex(int arrayIndex) {
    this.arrayIndex = arrayIndex;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (arrayElement ? 1231 : 1237);
    result = prime * result + arrayIndex;
    result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    RawDataElement other = (RawDataElement) obj;
    if (arrayElement != other.arrayElement) {
      return false;
    }
    if (arrayIndex != other.arrayIndex) {
      return false;
    }
    if (dataType == null) {
      if (other.dataType != null) {
        return false;
      }
    }
    else if (!dataType.equals(other.dataType)) {
      return false;
    }
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    }
    else if (!name.equals(other.name)) {
      return false;
    }
    if (value == null) {
      if (other.value != null) {
        return false;
      }
    }
    else if (!value.equals(other.value)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "RawDataElement [arrayElement=" + arrayElement + ", arrayIndex=" + arrayIndex + ", name=" + name + ", dataType=" + dataType + ", value=" + value + "]";
  }

}
