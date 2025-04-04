/**
 * Created Feb 25, 2021
 */
package com.ilardi.systems.jefs;

import java.io.Serializable;

/**
 * @author robert.ilardi
 *
 */

public class JefsPageHeader extends JefsBaseObjectInfo implements Serializable {

  private final int pageHeaderSize;

  private final int pageDataSize;

  private long objectPageIndex;

  private int usedDataSize;

  private long nextPagePosition;

  private long parentPagePosition;

  public JefsPageHeader(JefsBaseObjectInfo baseObjectInfo, int pageHeaderSize, int pageDataSize, long objectPageIndex, int usedDataSize, long nextPagePosition, long parentPagePosition) {
    this(baseObjectInfo.objectId, baseObjectInfo.position, baseObjectInfo.createdTs, baseObjectInfo.parentObjectId, baseObjectInfo.objectName, baseObjectInfo.objectType, baseObjectInfo.statusCode,
        baseObjectInfo.modifiedTs, pageHeaderSize, pageDataSize, objectPageIndex, usedDataSize, nextPagePosition, parentPagePosition);
  }

  public JefsPageHeader(JefsPageHeader other) {
    this(other.objectId, other.position, other.createdTs, other.parentObjectId, other.objectName, other.objectType, other.statusCode, other.modifiedTs, other.pageHeaderSize, other.pageDataSize,
        other.objectPageIndex, other.usedDataSize, other.nextPagePosition, other.getParentPagePosition());
  }

  public JefsPageHeader(long objectId, long position, long createdTs, long parentObjectId, String objectName, JefsObjectType objectType, JefsStatusCodes statusCode, long modifiedTs,
      int pageHeaderSize, int pageDataSize, long objectPageIndex, int usedDataSize, long nextPagePosition, long parentPagePosition) {
    super(objectId, position, createdTs, parentObjectId, objectName, objectType, statusCode, modifiedTs);

    this.pageHeaderSize = pageHeaderSize;
    this.pageDataSize = pageDataSize;
    this.objectPageIndex = objectPageIndex;
    this.usedDataSize = usedDataSize;
    this.nextPagePosition = nextPagePosition;
    this.parentPagePosition = parentPagePosition;
  }

  public int getUsedDataSize() {
    return usedDataSize;
  }

  public void setUsedDataSize(int usedDataSize) {
    this.usedDataSize = usedDataSize;
  }

  public long getModifiedTs() {
    return modifiedTs;
  }

  public void setModifiedTs(long modifiedTs) {
    this.modifiedTs = modifiedTs;
  }

  public long getNextPagePosition() {
    return nextPagePosition;
  }

  public void setNextPagePosition(long nextPagePosition) {
    this.nextPagePosition = nextPagePosition;
  }

  public int getPageHeaderSize() {
    return pageHeaderSize;
  }

  public int getPageDataSize() {
    return pageDataSize;
  }

  public long getObjectPageIndex() {
    return objectPageIndex;
  }

  public long getParentPagePosition() {
    return parentPagePosition;
  }

  public void setParentPagePosition(long parentPagePosition) {
    this.parentPagePosition = parentPagePosition;
  }

  public void setObjectPageIndex(long objectPageIndex) {
    this.objectPageIndex = objectPageIndex;
  }

  @Override
  public String toString() {
    return "JefsPageHeader [objectId=" + objectId + ", position=" + position + ", parentObjectId=" + parentObjectId + ", objectName=" + objectName + ", createdTs=" + createdTs + ", modifiedTs="
        + modifiedTs + ", objectType=" + objectType + ", statusCode=" + statusCode + ", nextPagePosition=" + nextPagePosition + ", parentPagePosition=" + parentPagePosition + ", objectPageIndex="
        + objectPageIndex + ", usedDataSize=" + usedDataSize + ", pageDataSize=" + pageDataSize + ", pageHeaderSize=" + pageHeaderSize + "]";
  }

}
