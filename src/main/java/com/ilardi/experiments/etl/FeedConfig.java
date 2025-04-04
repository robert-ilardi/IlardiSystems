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

@XmlType(name = "FeedConfig")
public class FeedConfig implements Serializable {

  protected Long feedId;

  protected String logicalName;
  protected String physicalName;
  protected String description;

  protected String feedType;
  protected boolean isActive;

  protected String formatType;

  protected String schema;
  protected String schemaType;

  protected List<FeedRecordConfig> recordConfig;

  protected SourceInfo sourceInfo;

  public FeedConfig() {}

  public Long getFeedId() {
    return feedId;
  }

  public void setFeedId(Long feedId) {
    this.feedId = feedId;
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

  public String getFeedType() {
    return feedType;
  }

  public void setFeedType(String feedType) {
    this.feedType = feedType;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
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

  public List<FeedRecordConfig> getRecordConfig() {
    return recordConfig;
  }

  public void setRecordConfig(List<FeedRecordConfig> recordConfig) {
    this.recordConfig = recordConfig;
  }

  public SourceInfo getSourceInfo() {
    return sourceInfo;
  }

  public void setSourceInfo(SourceInfo sourceInfo) {
    this.sourceInfo = sourceInfo;
  }

}
