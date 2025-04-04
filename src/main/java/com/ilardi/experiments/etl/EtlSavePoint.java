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

@XmlType(name = "EtlSavePoint")
public class EtlSavePoint implements Serializable {

  // Unique Save Point Id
  protected Long savePointRefId;

  // Feed Info
  protected Long feedId;

  // Record Info
  protected Long recordPosition;
  protected Long recordSequenceNumber;
  protected String recordName;

  // Table Info
  protected Long conceptId;

  // Row Info
  protected Long rowIndex;

  protected EtlSavePointState state;

  public EtlSavePoint() {}

  public Long getSavePointRefId() {
    return savePointRefId;
  }

  public void setSavePointRefId(Long savePointRefId) {
    this.savePointRefId = savePointRefId;
  }

  public Long getFeedId() {
    return feedId;
  }

  public void setFeedId(Long feedId) {
    this.feedId = feedId;
  }

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

  public Long getConceptId() {
    return conceptId;
  }

  public void setConceptId(Long conceptId) {
    this.conceptId = conceptId;
  }

  public Long getRowIndex() {
    return rowIndex;
  }

  public void setRowIndex(Long rowIndex) {
    this.rowIndex = rowIndex;
  }

  public EtlSavePointState getState() {
    return state;
  }

  public void setState(EtlSavePointState state) {
    this.state = state;
  }

}
