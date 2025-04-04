/**
 * Created Apr 1, 2021
 */
package com.ilardi.experiments.dbms.engine;

import java.io.Serializable;
import java.util.Properties;

import com.ilardi.experiments.dbms.transport.DbmsTransportType;

/**
 * @author robert.ilardi
 *
 */

public class DbmsInfo implements Serializable {

  private Properties dbmsProperties;

  private String dbmsName;

  private DbmsType dbmsType;

  private String systemDatabaseJefsFilePath;

  private DbmsTransportType transportType;

  private String authenticationManagerClassname;

  private String authorizationManagerClassname;

  private String transportManagerClassname;

  private String storageManagerClassname;

  private String sessionManagerClassname;

  private String queryManagerClassname;

  private String securityManagerClassname;

  private String endpointManagerClassname;

  private String mediatorClassname;

  public DbmsInfo() {}

  public String getSystemDatabaseJefsFilePath() {
    return systemDatabaseJefsFilePath;
  }

  public void setSystemDatabaseJefsFilePath(String systemDatabaseJefsFilePath) {
    this.systemDatabaseJefsFilePath = systemDatabaseJefsFilePath;
  }

  public String getDbmsName() {
    return dbmsName;
  }

  public void setDbmsName(String dbmsName) {
    this.dbmsName = dbmsName;
  }

  public DbmsType getDbmsType() {
    return dbmsType;
  }

  public void setDbmsType(DbmsType dbmsType) {
    this.dbmsType = dbmsType;
  }

  public DbmsTransportType getTransportType() {
    return transportType;
  }

  public void setTransportType(DbmsTransportType transportType) {
    this.transportType = transportType;
  }

  public String getAuthenticationManagerClassname() {
    return authenticationManagerClassname;
  }

  public void setAuthenticationManagerClassname(String authenticationManagerClassname) {
    this.authenticationManagerClassname = authenticationManagerClassname;
  }

  public String getAuthorizationManagerClassname() {
    return authorizationManagerClassname;
  }

  public void setAuthorizationManagerClassname(String authorizationManagerClassname) {
    this.authorizationManagerClassname = authorizationManagerClassname;
  }

  public Properties getDbmsProperties() {
    return dbmsProperties;
  }

  public void setDbmsProperties(Properties dbmsProperties) {
    this.dbmsProperties = dbmsProperties;
  }

  public String getTransportManagerClassname() {
    return transportManagerClassname;
  }

  public void setTransportManagerClassname(String transportManagerClassname) {
    this.transportManagerClassname = transportManagerClassname;
  }

  public String getStorageManagerClassname() {
    return storageManagerClassname;
  }

  public void setStorageManagerClassname(String storageManagerClassname) {
    this.storageManagerClassname = storageManagerClassname;
  }

  public String getSessionManagerClassname() {
    return sessionManagerClassname;
  }

  public void setSessionManagerClassname(String sessionManagerClassname) {
    this.sessionManagerClassname = sessionManagerClassname;
  }

  public String getQueryManagerClassname() {
    return queryManagerClassname;
  }

  public void setQueryManagerClassname(String queryManagerClassname) {
    this.queryManagerClassname = queryManagerClassname;
  }

  public String getSecurityManagerClassname() {
    return securityManagerClassname;
  }

  public void setSecurityManagerClassname(String securityManagerClassname) {
    this.securityManagerClassname = securityManagerClassname;
  }

  public String getEndpointManagerClassname() {
    return endpointManagerClassname;
  }

  public void setEndpointManagerClassname(String endpointManagerClassname) {
    this.endpointManagerClassname = endpointManagerClassname;
  }

  public String getMediatorClassname() {
    return mediatorClassname;
  }

  public void setMediatorClassname(String mediatorClassname) {
    this.mediatorClassname = mediatorClassname;
  }

  @Override
  public String toString() {
    return "DbmsInfo [dbmsName=" + dbmsName + ", dbmsType=" + dbmsType + ", systemDatabaseJefsFilePath=" + systemDatabaseJefsFilePath + ", transportType=" + transportType
        + ", authenticationManagerClassname=" + authenticationManagerClassname + ", authorizationManagerClassname=" + authorizationManagerClassname + ", transportManagerClassname="
        + transportManagerClassname + ", storageManagerClassname=" + storageManagerClassname + ", sessionManagerClassname=" + sessionManagerClassname + ", queryManagerClassname="
        + queryManagerClassname + ", securityManagerClassname=" + securityManagerClassname + ", endpointManagerClassname=" + endpointManagerClassname + ", mediatorClassname=" + mediatorClassname
        + "]";
  }

}
