/**
 * Created Jun 20, 2017
 */

package com.ilardi.experiments.etl;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

import com.ilardi.systems.metadata.Concept;

/**
 * @author rilardi
 *
 */

@XmlType(name = "EtlTable")
public class EtlTable implements Serializable {

  protected Concept meta;

  protected List<EtlRow> rows;

  protected EtlTableOperationType operationType;

  public EtlTable() {}

  public Concept getMeta() {
    return meta;
  }

  public void setMeta(Concept meta) {
    this.meta = meta;
  }

  public EtlTableOperationType getOperationType() {
    return operationType;
  }

  public void setOperationType(EtlTableOperationType operationType) {
    this.operationType = operationType;
  }

  public List<EtlRow> getRows() {
    return rows;
  }

  public void setRows(List<EtlRow> rows) {
    this.rows = rows;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((meta == null) ? 0 : meta.hashCode());
    result = prime * result + ((operationType == null) ? 0 : operationType.hashCode());
    result = prime * result + ((rows == null) ? 0 : rows.hashCode());
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
    EtlTable other = (EtlTable) obj;
    if (meta == null) {
      if (other.meta != null) {
        return false;
      }
    }
    else if (!meta.equals(other.meta)) {
      return false;
    }
    if (operationType != other.operationType) {
      return false;
    }
    if (rows == null) {
      if (other.rows != null) {
        return false;
      }
    }
    else if (!rows.equals(other.rows)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "EtlTable [meta=" + meta + ", rows=" + rows + ", operationType=" + operationType + "]";
  }

}
