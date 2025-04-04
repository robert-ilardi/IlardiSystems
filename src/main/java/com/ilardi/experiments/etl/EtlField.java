/**
 * Created Jun 20, 2017
 */

package com.ilardi.experiments.etl;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

import com.ilardi.systems.metadata.Attribute;

/**
 * @author rilardi
 *
 */

@XmlType(name = "EtlField")
public class EtlField implements Serializable {

  protected Long fieldIndex;

  protected Attribute meta;

  protected Object value;

  public EtlField() {}

  public Long getFieldIndex() {
    return fieldIndex;
  }

  public void setFieldIndex(Long fieldIndex) {
    this.fieldIndex = fieldIndex;
  }

  public Attribute getMeta() {
    return meta;
  }

  public void setMeta(Attribute meta) {
    this.meta = meta;
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
    result = prime * result + ((fieldIndex == null) ? 0 : fieldIndex.hashCode());
    result = prime * result + ((meta == null) ? 0 : meta.hashCode());
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
    EtlField other = (EtlField) obj;
    if (fieldIndex == null) {
      if (other.fieldIndex != null) {
        return false;
      }
    }
    else if (!fieldIndex.equals(other.fieldIndex)) {
      return false;
    }
    if (meta == null) {
      if (other.meta != null) {
        return false;
      }
    }
    else if (!meta.equals(other.meta)) {
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
    return "EtlField [fieldIndex=" + fieldIndex + ", meta=" + meta + ", value=" + value + "]";
  }

}
