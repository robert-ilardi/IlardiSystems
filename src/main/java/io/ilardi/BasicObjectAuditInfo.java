/**
 * Created Nov 2, 2025
 */
package io.ilardi;

import java.time.LocalDateTime;

/**
 * @Author rober
 */

public class BasicObjectAuditInfo implements ValueObject {

  private String creationUser;
  private String creationApp;
  private LocalDateTime creationTs;

  private String lastModUser;
  private String lastModApp;
  private LocalDateTime lastModTs;
  private LastModificationChangeCodes lastModChgCd;

  public BasicObjectAuditInfo() {
    super();
  }

  public String getCreationUser() {
    return creationUser;
  }

  public void setCreationUser(String creationUser) {
    this.creationUser = creationUser;
  }

  public String getCreationApp() {
    return creationApp;
  }

  public void setCreationApp(String creationApp) {
    this.creationApp = creationApp;
  }

  public LocalDateTime getCreationTs() {
    return creationTs;
  }

  public void setCreationTs(LocalDateTime creationTs) {
    this.creationTs = creationTs;
  }

  public String getLastModUser() {
    return lastModUser;
  }

  public void setLastModUser(String lastModUser) {
    this.lastModUser = lastModUser;
  }

  public String getLastModApp() {
    return lastModApp;
  }

  public void setLastModApp(String lastModApp) {
    this.lastModApp = lastModApp;
  }

  public LocalDateTime getLastModTs() {
    return lastModTs;
  }

  public void setLastModTs(LocalDateTime lastModTs) {
    this.lastModTs = lastModTs;
  }

  public LastModificationChangeCodes getLastModChgCd() {
    return lastModChgCd;
  }

  public void setLastModChgCd(LastModificationChangeCodes lastModChgCd) {
    this.lastModChgCd = lastModChgCd;
  }

  @Override
  public int compareTo(ValueObject o) {
    // TODO Auto-generated method stub
    return 0;
  }

}
