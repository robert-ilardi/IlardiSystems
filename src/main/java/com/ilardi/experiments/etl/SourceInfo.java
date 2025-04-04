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

@XmlType(name = "SourceInfo")
public class SourceInfo implements Serializable {

  protected Long sourceId;

  protected String logicalName;
  protected String physicalName;
  protected String description;

  protected String sourceType; // File, Message, Database, etc.
  protected String sourceSubType; // XML File, CSV File, MQ Message, TibcoRV Message, SQL Database, NoSQL
                                  // Database, etc.

  protected boolean isActive;

  protected String host;
  protected Integer port;

  protected String url;
  protected String path;

  protected Map<String, String> sourceProperties;

  protected String subject;
  protected String topicName;
  protected String queueName;

  protected String principal;
  protected String credentials;

  public SourceInfo() {}

  public Long getSourceId() {
    return sourceId;
  }

  public void setSourceId(Long sourceId) {
    this.sourceId = sourceId;
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

  public String getSourceType() {
    return sourceType;
  }

  public void setSourceType(String sourceType) {
    this.sourceType = sourceType;
  }

  public String getSourceSubType() {
    return sourceSubType;
  }

  public void setSourceSubType(String sourceSubType) {
    this.sourceSubType = sourceSubType;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Map<String, String> getSourceProperties() {
    return sourceProperties;
  }

  public void setSourceProperties(Map<String, String> sourceProperties) {
    this.sourceProperties = sourceProperties;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getTopicName() {
    return topicName;
  }

  public void setTopicName(String topicName) {
    this.topicName = topicName;
  }

  public String getQueueName() {
    return queueName;
  }

  public void setQueueName(String queueName) {
    this.queueName = queueName;
  }

  public String getPrincipal() {
    return principal;
  }

  public void setPrincipal(String principal) {
    this.principal = principal;
  }

  public String getCredentials() {
    return credentials;
  }

  public void setCredentials(String credentials) {
    this.credentials = credentials;
  }

}
