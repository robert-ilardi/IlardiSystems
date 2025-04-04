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

@XmlType(name = "FeedRecordConfig")
public class FeedRecordConfig implements Serializable {

  protected Long feedId;
  protected Long recordId;

  protected String logicalName;
  protected String physicalName;
  protected String description;

  protected String recordType;
  protected String formatType;

  protected String schema;
  protected String schemaType;

  protected List<FeedFieldConfig> fieldConfig;

  public FeedRecordConfig() {}

  public Long getFeedId() {
    return feedId;
  }

  public void setFeedId(Long feedId) {
    this.feedId = feedId;
  }

  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }

  public String getLogicalName() {
    return logicalName;
  }

  public void setLogicalName(String logicalName) {
    this.logicalName = logicalName;
  }

  public String getPhysicalName() {
    return physicalName;
  }

  public void setPhysicalName(String physicalName) {
    this.physicalName = physicalName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getRecordType() {
    return recordType;
  }

  public void setRecordType(String recordType) {
    this.recordType = recordType;
  }

  public String getFormatType() {
    return formatType;
  }

  public void setFormatType(String formatType) {
    this.formatType = formatType;
  }

  public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public String getSchemaType() {
    return schemaType;
  }

  public void setSchemaType(String schemaType) {
    this.schemaType = schemaType;
  }

  public List<FeedFieldConfig> getFieldConfig() {
    return fieldConfig;
  }

  public void setFieldConfig(List<FeedFieldConfig> fieldConfig) {
    this.fieldConfig = fieldConfig;
  }

}
