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

@XmlType(name = "FeedFieldConfig")
public class FeedFieldConfig implements Serializable {

  protected Long feedId;
  protected Long recordId;
  protected Long fieldId;

  protected String logicalName;
  protected String physicalName;
  protected String description;
  protected String fieldType;

  protected String dataType;
  protected boolean arrayType;

  protected String syntax;

  protected String position;

  protected Integer length;
  protected Integer precision;

  protected String regEx;

  protected String schema;
  protected String schemaType;

  public FeedFieldConfig() {}

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

  public Long getFieldId() {
    return fieldId;
  }

  public void setFieldId(Long fieldId) {
    this.fieldId = fieldId;
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

  public String getFieldType() {
    return fieldType;
  }

  public void setFieldType(String fieldType) {
    this.fieldType = fieldType;
  }

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public boolean isArrayType() {
    return arrayType;
  }

  public void setArrayType(boolean arrayType) {
    this.arrayType = arrayType;
  }

  public String getSyntax() {
    return syntax;
  }

  public void setSyntax(String syntax) {
    this.syntax = syntax;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public Integer getPrecision() {
    return precision;
  }

  public void setPrecision(Integer precision) {
    this.precision = precision;
  }

  public String getRegEx() {
    return regEx;
  }

  public void setRegEx(String regEx) {
    this.regEx = regEx;
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

}
