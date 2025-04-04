/**
 * Created Jun 20, 2017
 */
package com.ilardi.experiments.etl;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

/**
 * @author rilardi
 *
 */

@XmlType(name = "RawHeader")
public class RawHeader implements Serializable {

  protected Long recordPosition;

  protected Long recordSequenceNumber;

  protected String recordName;

  protected Map<String, String> headers;

  public RawHeader() {}

  public Long getRecordPosition() {
    return recordPosition;
  }

  public void setRecordPosition(Long recordPosition) {
    this.recordPosition = recordPosition;
  }

  public Long getRecordSequenceNumber() {
    return recordSequenceNumber;
  }

  public void setRecordSequenceNumber(Long recordSequenceNumber) {
    this.recordSequenceNumber = recordSequenceNumber;
  }

  public String getRecordName() {
    return recordName;
  }

  public void setRecordName(String recordName) {
    this.recordName = recordName;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((headers == null) ? 0 : headers.hashCode());
    result = prime * result + ((recordName == null) ? 0 : recordName.hashCode());
    result = prime * result + ((recordPosition == null) ? 0 : recordPosition.hashCode());
    result = prime * result + ((recordSequenceNumber == null) ? 0 : recordSequenceNumber.hashCode());
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
    RawHeader other = (RawHeader) obj;
    if (headers == null) {
      if (other.headers != null) {
        return false;
      }
    }
    else if (!headers.equals(other.headers)) {
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
    if (recordPosition == null) {
      if (other.recordPosition != null) {
        return false;
      }
    }
    else if (!recordPosition.equals(other.recordPosition)) {
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
    return true;
  }

  @Override
  public String toString() {
    return "RawHeader [recordPosition=" + recordPosition + ", recordSequenceNumber=" + recordSequenceNumber + ", recordName=" + recordName + ", headers=" + headers + "]";
  }

}
