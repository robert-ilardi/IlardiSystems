/**
 * Created Feb 25, 2021
 */
package com.ilardi.systems.jefs;

import java.io.Serializable;

/**
 * @author robert.ilardi
 *
 */

public class JefsFileSystemInfo implements Serializable {

  private final int volHeaderSize;

  private final String jefsMagicBytes;
  private final int jefsVersion;

  private final int pageHeaderSize;
  private final int pageDataSize;

  private final long creationTs;

  private final String physicalFilePath;

  private String volumeName;

  private long modificationTs;

  private long nextObjectId;

  private long freeChainBeginPosition;
  private long freeChainEndPosition;

  public JefsFileSystemInfo(int volHeaderSize, String jefsMagicBytes, int jefsVersion, int pageHeaderSize, int pageDataSize, long creationTs, String physicalFilePath) {
    this.volHeaderSize = volHeaderSize;

    this.jefsMagicBytes = jefsMagicBytes;
    this.jefsVersion = jefsVersion;

    this.pageHeaderSize = pageHeaderSize;
    this.pageDataSize = pageDataSize;

    this.creationTs = creationTs;

    this.physicalFilePath = physicalFilePath;
  }

  public String getVolumeName() {
    return volumeName;
  }

  public void setVolumeName(String volumeName) {
    this.volumeName = volumeName;
  }

  public long getModificationTs() {
    return modificationTs;
  }

  public void setModificationTs(long modificationTs) {
    this.modificationTs = modificationTs;
  }

  public long getNextObjectId() {
    return nextObjectId;
  }

  public void setNextObjectId(long nextObjectId) {
    this.nextObjectId = nextObjectId;
  }

  public long getFreeChainBeginPosition() {
    return freeChainBeginPosition;
  }

  public void setFreeChainBeginPosition(long freeChainBeginPosition) {
    this.freeChainBeginPosition = freeChainBeginPosition;
  }

  public long getFreeChainEndPosition() {
    return freeChainEndPosition;
  }

  public void setFreeChainEndPosition(long freeChainEndPosition) {
    this.freeChainEndPosition = freeChainEndPosition;
  }

  public int getVolHeaderSize() {
    return volHeaderSize;
  }

  public String getJefsMagicBytes() {
    return jefsMagicBytes;
  }

  public int getJefsVersion() {
    return jefsVersion;
  }

  public int getPageHeaderSize() {
    return pageHeaderSize;
  }

  public int getPageDataSize() {
    return pageDataSize;
  }

  public long getCreationTs() {
    return creationTs;
  }

  public String getPhysicalFilePath() {
    return physicalFilePath;
  }

  public int getTotalPageSize() {
    return pageHeaderSize + pageDataSize;
  }

  @Override
  public String toString() {
    return "JefsFileSystemInfo [volHeaderSize=" + volHeaderSize + ", jefsMagicBytes=" + jefsMagicBytes + ", jefsVersion=" + jefsVersion + ", pageHeaderSize=" + pageHeaderSize + ", pageDataSize="
        + pageDataSize + ", creationTs=" + creationTs + ", physicalFilePath=" + physicalFilePath + ", volumeName=" + volumeName + ", modificationTs=" + modificationTs + ", nextObjectId="
        + nextObjectId + ", freeChainBeginPosition=" + freeChainBeginPosition + ", freeChainEndPosition=" + freeChainEndPosition + "]";
  }

}
