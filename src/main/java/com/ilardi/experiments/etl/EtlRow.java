/**
 * Created Jun 20, 2017
 */

package com.ilardi.experiments.etl;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

import com.ilardi.systems.metadata.Attribute;

/**
 * @author rilardi
 *
 */

@XmlType(name = "EtlField")
public class EtlRow implements Serializable {

  protected Long rowIndex;

  protected List<Attribute> meta;

  protected List<EtlField> fields;

  public EtlRow() {}

  public Long getRowIndex() {
    return rowIndex;
  }

  public void setRowIndex(Long rowIndex) {
    this.rowIndex = rowIndex;
  }

  public List<Attribute> getMeta() {
    return meta;
  }

  public void setMeta(List<Attribute> meta) {
    this.meta = meta;
  }

  public List<EtlField> getFields() {
    return fields;
  }

  public void setFields(List<EtlField> fields) {
    this.fields = fields;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fields == null) ? 0 : fields.hashCode());
    result = prime * result + ((meta == null) ? 0 : meta.hashCode());
    result = prime * result + ((rowIndex == null) ? 0 : rowIndex.hashCode());
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
    EtlRow other = (EtlRow) obj;
    if (fields == null) {
      if (other.fields != null) {
        return false;
      }
    }
    else if (!fields.equals(other.fields)) {
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
    if (rowIndex == null) {
      if (other.rowIndex != null) {
        return false;
      }
    }
    else if (!rowIndex.equals(other.rowIndex)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "EtlRow [rowIndex=" + rowIndex + ", meta=" + meta + ", fields=" + fields + "]";
  }

}
