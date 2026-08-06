/**
 * Created Aug 6, 2026
 */
package io.ilardi;

/**
 * @author Kate Ilardi
 */

public class AuthenticationInfo implements ValueObject {

  private String subject;

  private String resource;

  private String role;

  private String resourceKey;
  private String resourceKeyType;
  private String resourceKeyAlgo;

  private String principal;

  private String credentials;
  private String credentialsType;
  private String createntialsAlgo;

  private String uniqueToken;

  public AuthenticationInfo() {
    super();
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getResourceKey() {
    return resourceKey;
  }

  public void setResourceKey(String resourceKey) {
    this.resourceKey = resourceKey;
  }

  public String getResourceKeyType() {
    return resourceKeyType;
  }

  public void setResourceKeyType(String resourceKeyType) {
    this.resourceKeyType = resourceKeyType;
  }

  public String getResourceKeyAlgo() {
    return resourceKeyAlgo;
  }

  public void setResourceKeyAlgo(String resourceKeyAlgo) {
    this.resourceKeyAlgo = resourceKeyAlgo;
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

  public String getCredentialsType() {
    return credentialsType;
  }

  public void setCredentialsType(String credentialsType) {
    this.credentialsType = credentialsType;
  }

  public String getCreatentialsAlgo() {
    return createntialsAlgo;
  }

  public void setCreatentialsAlgo(String createntialsAlgo) {
    this.createntialsAlgo = createntialsAlgo;
  }

  public String getUniqueToken() {
    return uniqueToken;
  }

  public void setUniqueToken(String uniqueToken) {
    this.uniqueToken = uniqueToken;
  }

  @Override
  public int compareTo(ValueObject other) {
    // TODO Auto-generated method stub
    return 0;
  }

}
