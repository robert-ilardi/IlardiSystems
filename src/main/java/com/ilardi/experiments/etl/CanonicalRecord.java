/**
 * Created Jun 20, 2017
 */
package com.ilardi.experiments.etl;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

/**
 * @author rilardi
 *
 */

@XmlType(name = "CanonicalRecord")
public class CanonicalRecord implements Serializable {

  protected String recordName;

  protected Long recordId;

  protected Long recordSequenceNumber;

  protected List<EtlTable> tables;

  protected List<CanonicalRecord> subRecords;

  public CanonicalRecord() {}

  public String getRecordName() {
    return recordName;
  }

  public void setRecordName(String recordName) {
    this.recordName = recordName;
  }

  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }

  public Long getRecordSequenceNumber() {
    return recordSequenceNumber;
  }

  public void setRecordSequenceNumber(Long recordSequenceNumber) {
    this.recordSequenceNumber = recordSequenceNumber;
  }

  public List<CanonicalRecord> getSubRecords() {
    return subRecords;
  }

  public void setSubRecords(List<CanonicalRecord> subRecords) {
    this.subRecords = subRecords;
  }

  public List<EtlTable> getTables() {
    return tables;
  }

  public void setTables(List<EtlTable> tables) {
    this.tables = tables;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((recordId == null) ? 0 : recordId.hashCode());
    result = prime * result + ((recordName == null) ? 0 : recordName.hashCode());
    result = prime * result + ((recordSequenceNumber == null) ? 0 : recordSequenceNumber.hashCode());
    result = prime * result + ((subRecords == null) ? 0 : subRecords.hashCode());
    result = prime * result + ((tables == null) ? 0 : tables.hashCode());
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
    CanonicalRecord other = (CanonicalRecord) obj;
    if (recordId == null) {
      if (other.recordId != null) {
        return false;
      }
    }
    else if (!recordId.equals(other.recordId)) {
      return false;
    }
    if (recordName == null) {
      if (other.recordName != null) {
        return false;
      }
    }
    else if (!recordName.equals(other.recordName)) {
      return false;
    }
    if (recordSequenceNumber == null) {
      if (other.recordSequenceNumber != null) {
        return false;
      }
    }
    else if (!recordSequenceNumber.equals(other.recordSequenceNumber)) {
      return false;
    }
    if (subRecords == null) {
      if (other.subRecords != null) {
        return false;
      }
    }
    else if (!subRecords.equals(other.subRecords)) {
      return false;
    }
    if (tables == null) {
      if (other.tables != null) {
        return false;
      }
    }
    else if (!tables.equals(other.tables)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CanonicalRecord [recordName=" + recordName + ", recordId=" + recordId + ", recordSequenceNumber=" + recordSequenceNumber + ", tables=" + tables + ", subRecords=" + subRecords + "]";
  }

}
