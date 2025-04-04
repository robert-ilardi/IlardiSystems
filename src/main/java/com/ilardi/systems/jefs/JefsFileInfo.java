/**
 * Created Feb 25, 2021
 */
package com.ilardi.systems.jefs;

import java.io.Serializable;

/**
 * @author robert.ilardi
 *
 */

public class JefsFileInfo extends JefsBaseObjectInfo implements Serializable {

  private String fullPath;

  public JefsFileInfo(JefsBaseObjectInfo baseObjectInfo, String fullPath) {
    this(baseObjectInfo.objectId, baseObjectInfo.position, baseObjectInfo.createdTs, baseObjectInfo.parentObjectId, baseObjectInfo.objectName, baseObjectInfo.objectType, baseObjectInfo.statusCode,
        baseObjectInfo.modifiedTs, fullPath);
  }

  public JefsFileInfo(JefsFileInfo other) {
    this(other.objectId, other.position, other.createdTs, other.parentObjectId, other.objectName, other.objectType, other.statusCode, other.modifiedTs, other.fullPath);
  }

  public JefsFileInfo(long jefsObjectId, long position, long createdTs, long jefsParentObjectId, String jefsObjectName, JefsObjectType objectType, JefsStatusCodes statusCode, long modifiedTs,
      String fullPath) {
    super(jefsObjectId, position, createdTs, jefsParentObjectId, jefsObjectName, objectType, statusCode, modifiedTs);

    this.fullPath = fullPath;
  }

  public String getFullPath() {
    return fullPath;
  }

  public void setFullPath(String fullPath) {
    this.fullPath = fullPath;
  }

  public void update(JefsPageHeader header) {
    update(header.getParentObjectId(), header.getObjectName(), header.getObjectType(), header.getStatusCode(), header.getModifiedTs());
  }

  @Override
  public String toString() {
    return "JefsFileInfo [fullPath=" + fullPath + ", objectId=" + objectId + ", position=" + position + ", createdTs=" + createdTs + ", parentObjectId=" + parentObjectId + ", objectName=" + objectName
        + ", objectType=" + objectType + ", statusCode=" + statusCode + ", modifiedTs=" + modifiedTs + "]";
  }

}
