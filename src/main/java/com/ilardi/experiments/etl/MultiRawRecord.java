/**
 * Created Jun 20, 2017
 */
package com.ilardi.experiments.etl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

/**
 * @author rilardi
 *
 */

@XmlType(name = "MultiRawRecord")
public class MultiRawRecord implements Serializable {

  protected Long recordSetId;

  protected Long recordSetSequenceNumber;

  protected String recordSetName;

  protected List<RawRecord> records;

  public MultiRawRecord() {}

  public List<RawRecord> getRecords() {
    return records;
  }

  public void setRecords(List<RawRecord> records) {
    this.records = records;
  }

  public Long getRecordSetId() {
    return recordSetId;
  }

  public void setRecordSetId(Long recordSetId) {
    this.recordSetId = recordSetId;
  }

  public Long getRecordSetSequenceNumber() {
    return recordSetSequenceNumber;
  }

  public void setRecordSetSequenceNumber(Long recordSetSequenceNumber) {
    this.recordSetSequenceNumber = recordSetSequenceNumber;
  }

  public String getRecordSetName() {
    return recordSetName;
  }

  public void setRecordSetName(String recordSetName) {
    this.recordSetName = recordSetName;
  }

  public synchronized void addRecord(RawRecord rawRec) {
    if (records == null) {
      records = new ArrayList<RawRecord>();
    }

    records.add(rawRec);
  }

  public synchronized int recordCount() {
    return (records != null ? records.size() : -1);
  }

  public synchronized void clearRecords() {
    if (records != null) {
      records.clear();
    }
  }

  public synchronized RawRecord getRecord(int index) {
    return records.get(index);
  }

  public synchronized RawRecord removeRecord(int index) {
    return records.remove(index);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((recordSetId == null) ? 0 : recordSetId.hashCode());
    result = prime * result + ((recordSetName == null) ? 0 : recordSetName.hashCode());
    result = prime * result + ((recordSetSequenceNumber == null) ? 0 : recordSetSequenceNumber.hashCode());
    result = prime * result + ((records == null) ? 0 : records.hashCode());
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
    MultiRawRecord other = (MultiRawRecord) obj;
    if (recordSetId == null) {
      if (other.recordSetId != null) {
        return false;
      }
    }
    else if (!recordSetId.equals(other.recordSetId)) {
      return false;
    }
    if (recordSetName == null) {
      if (other.recordSetName != null) {
        return false;
      }
    }
    else if (!recordSetName.equals(other.recordSetName)) {
      return false;
    }
    if (recordSetSequenceNumber == null) {
      if (other.recordSetSequenceNumber != null) {
        return false;
      }
    }
    else if (!recordSetSequenceNumber.equals(other.recordSetSequenceNumber)) {
      return false;
    }
    if (records == null) {
      if (other.records != null) {
        return false;
      }
    }
    else if (!records.equals(other.records)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "MultiRawRecord [recordSetId=" + recordSetId + ", recordSetSequenceNumber=" + recordSetSequenceNumber + ", recordSetName=" + recordSetName + ", records=" + records + "]";
  }

}
