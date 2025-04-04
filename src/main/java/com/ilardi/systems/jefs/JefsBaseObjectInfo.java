/**
 * Created Feb 28, 2021
 */
package com.ilardi.systems.jefs;

import java.io.Serializable;

/**
 * @author robert.ilardi
 *
 */

public class JefsBaseObjectInfo implements Serializable {

  protected final long objectId;

  protected final long position;

  protected final long createdTs;

  protected long parentObjectId;

  protected String objectName;

  protected JefsObjectType objectType;

  protected JefsStatusCodes statusCode;

  protected long modifiedTs;

  public JefsBaseObjectInfo(JefsBaseObjectInfo other) {
    this(other.objectId, other.position, other.createdTs, other.parentObjectId, other.objectName, other.objectType, other.statusCode, other.modifiedTs);
  }

  public JefsBaseObjectInfo(long objectId, long position, long createdTs, long parentObjectId, String objectName, JefsObjectType objectType, JefsStatusCodes statusCode, long modifiedTs) {
    this.objectId = objectId;

    this.position = position;
    this.createdTs = createdTs;

    update(parentObjectId, objectName, objectType, statusCode, modifiedTs);
  }

  public void update(long parentObjectId, String objectName, JefsObjectType objectType, JefsStatusCodes statusCode, long modifiedTs) {
    this.parentObjectId = parentObjectId;

    this.objectName = objectName;

    this.objectType = objectType;
    this.statusCode = statusCode;

    this.modifiedTs = modifiedTs;
  }

  public long getParentObjectId() {
    return parentObjectId;
  }

  public void setParentObjectId(long parentObjectId) {
    this.parentObjectId = parentObjectId;
  }

  public String getObjectName() {
    return objectName;
  }

  public void setObjectName(String objectName) {
    this.objectName = objectName;
  }

  public JefsObjectType getObjectType() {
    return objectType;
  }

  public void setObjectType(JefsObjectType objectType) {
    this.objectType = objectType;
  }

  public JefsStatusCodes getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(JefsStatusCodes statusCode) {
    this.statusCode = statusCode;
  }

  public long getModifiedTs() {
    return modifiedTs;
  }

  public void setModifiedTs(long modifiedTs) {
    this.modifiedTs = modifiedTs;
  }

  public long getObjectId() {
    return objectId;
  }

  public long getPosition() {
    return position;
  }

  public long getCreatedTs() {
    return createdTs;
  }

  @Override
  public String toString() {
    return "JefsBaseObjectInfo [objectId=" + objectId + ", position=" + position + ", createdTs=" + createdTs + ", parentObjectId=" + parentObjectId + ", objectName=" + objectName + ", objectType="
        + objectType + ", statusCode=" + statusCode + ", modifiedTs=" + modifiedTs + "]";
  }

}
