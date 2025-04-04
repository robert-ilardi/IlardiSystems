/**
 * Created Apr 17, 2021
 */
package com.ilardi.systems.jefs;

import static com.ilardi.systems.jefs.JefsConstants.JEFS_MAGIC_BYTES;
import static com.ilardi.systems.jefs.JefsConstants.JEFS_MMS_PAGE_NAME;
import static com.ilardi.systems.jefs.JefsConstants.JEFS_VERSION;
import static com.ilardi.systems.jefs.JefsConstants.MMS_PAGE_ENTRY_LEN;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author robert.ilardi
 *
 */

public class JefsHeapFileSystem extends JefsPageFileSystem {

  public JefsHeapFileSystem(String physicalFilePath) throws IOException {
    this(physicalFilePath, null);
  }

  public JefsHeapFileSystem(String physicalFilePath, String volumeName) throws IOException {
    this(JEFS_MAGIC_BYTES, JEFS_VERSION, physicalFilePath, volumeName);
  }

  public JefsHeapFileSystem(String magicBytes, int version, String physicalFilePath, String volumeName) throws IOException {
    super(magicBytes, version, physicalFilePath, volumeName);
  }

  // START File System Init Functions------>

  protected void initNewFileSystem() throws IOException {
    synchronized (fsLock) {
      super.initNewFileSystem();
      createMemoryManagementSegment();
    }
  }

  private void createMemoryManagementSegment() throws IOException {
    synchronized (fsLock) {
      allocatePage(0, 0, JEFS_MMS_PAGE_NAME, JefsObjectType.MEMORY_MANAGEMENT_SEGMENT, 0, null);
    }
  }

  // END File System Init Functions-------->

  // START Public High Level Operations------>

  @Override
  public long getFileSystemFirstActiveDataPagePosition() throws IOException {
    return getFirstJefsDataPagePosition();
  }

  /*
   * @Override public List<Long> getRootPagePositions() throws IOException {
   * synchronized (fsLock) { ArrayList<Long> rootPagePositions; JefsPage mmsPage;
   * long position;
   * 
   * position = getFirstJefsMmsPagePosition();
   * 
   * rootPagePositions = new ArrayList<Long>();
   * 
   * while (position > 0) { mmsPage = readPageByPosition(position);
   * 
   * readAllRootPagePositions(mmsPage, rootPagePositions);
   * 
   * position = mmsPage.getNextPagePosition(); }
   * 
   * return rootPagePositions; } }
   */

  @Override
  public long getStartingRootPagePosition() throws IOException {
    return getFirstJefsMmsPagePosition();
  }

  @Override
  public List<Long> getNextRootPagePositions(long startingRootPagePos) throws IOException {
    synchronized (fsLock) {
      ArrayList<Long> rootPagePositions;
      JefsPage mmsPage;
      long position;

      position = startingRootPagePos;

      rootPagePositions = new ArrayList<Long>();

      if (position > 0) {
        mmsPage = readPageByPosition(position);

        readAllRootPagePositions(mmsPage, rootPagePositions);

        position = mmsPage.getNextPagePosition();

        rootPagePositions.add(0, position); // Add Next Root Page Position
      }

      return rootPagePositions;
    }
  }

  // END Public High Level Operations-------->

  // START Page Level I/O Operations------>

  @SuppressWarnings("unused")
  @Override
  protected void readAllRootPagePositions(JefsPage mmsPage, ArrayList<Long> rootPagePositions) throws IOException {
    synchronized (fsLock) {
      long mmsLoc, objPos, objId;
      ByteBuffer buf;

      buf = ByteBuffer.wrap(mmsPage.getData());

      objPos = 0;
      mmsLoc = 0;

      while (mmsLoc < fsInfo.getPageDataSize()) {
        objPos = buf.getLong();
        objId = buf.getLong();

        /*
         * if (objPos == 0) { break; } else { rootPagePositions.add(objPos); mmsLoc +=
         * MMS_PAGE_ENTRY_LEN; }
         */

        if (objPos != 0) {
          rootPagePositions.add(objPos);
        }

        mmsLoc += MMS_PAGE_ENTRY_LEN;
      }

      buf = null;
    }
  }

  protected void writePage(JefsPageHeader pageHeader, byte[] pageData) throws IOException {
    synchronized (fsLock) {
      if (pageHeader.getObjectType() == JefsObjectType.VARIABLE && pageHeader.getObjectPageIndex() == 0 && pageHeader.getStatusCode() != JefsStatusCodes.DELETED) {
        insertMemoryManagementSegmentEntry(pageHeader.getPosition(), pageHeader.getObjectId());
      }

      super.writePage(pageHeader, pageData);
    }
  }

  private void insertMemoryManagementSegmentEntry(long position, long objectId) throws IOException {
    synchronized (fsLock) {
      byte[] bArr;
      ByteBuffer buf;
      long mmsPos;

      mmsPos = findMmsLocationPosition(position);

      if (mmsPos > 0) {
        return;
      }

      mmsPos = findFreeMmsLocationPosition();

      bArr = new byte[MMS_PAGE_ENTRY_LEN];

      fillNulls(bArr);

      buf = ByteBuffer.wrap(bArr);

      buf.putLong(position);
      buf.putLong(objectId);

      writeData(bArr, mmsPos, true);

      buf = null;
      bArr = null;
    }
  }

  private long allocateNewMemoryManagementSegment(JefsPageHeader lastMmsHeader) throws IOException {
    synchronized (fsLock) {
      long freePatLocPos;
      JefsPageHeader mmsHeader;
      JefsPage mmsPage;

      mmsPage = allocatePage(lastMmsHeader.getParentObjectId(), lastMmsHeader.getParentPagePosition(), JEFS_MMS_PAGE_NAME, JefsObjectType.MEMORY_MANAGEMENT_SEGMENT,
          lastMmsHeader.getObjectPageIndex() + 1, null);

      mmsHeader = mmsPage.getHeader();

      freePatLocPos = mmsHeader.getPosition() + mmsHeader.getPageHeaderSize();

      return freePatLocPos;
    }
  }

  protected void deletePage(JefsPageHeader header, byte[] pageData) throws IOException {
    synchronized (fsLock) {
      if (header == null) {
        throw new IOException("Cannot Perform Delete Operation on NULL Header!");
      }

      if (header.getObjectType() == JefsObjectType.VARIABLE && header.getObjectPageIndex() == 0) {
        removeMemoryManagementSegmentEntry(header.getPosition());
      }

      super.deletePage(header, pageData);
    }
  }

  private void removeMemoryManagementSegmentEntry(long rootPagePosition) throws IOException {
    synchronized (fsLock) {
      byte[] bArr;
      long mmsPos;

      mmsPos = findMmsLocationPosition(rootPagePosition);

      if (mmsPos > 0) {
        bArr = new byte[MMS_PAGE_ENTRY_LEN];

        fillNulls(bArr);

        writeData(bArr, mmsPos, true);

        bArr = null;
      }
    }
  }

  // END Page Level I/O Operations-------->

  // START Object Search Operations------>

  protected long getFirstJefsDataPagePosition() throws IOException {
    synchronized (fsLock) {
      long objectFirstPagePosition;

      objectFirstPagePosition = getFirstJefsMmsPagePosition();
      objectFirstPagePosition += fsInfo.getTotalPageSize();

      return objectFirstPagePosition;
    }
  }

  protected long getFirstJefsMmsPagePosition() throws IOException {
    synchronized (fsLock) {
      long objectFirstPagePosition;

      objectFirstPagePosition = getFirstPageFsDataPagePosition();

      return objectFirstPagePosition;
    }
  }

  private long findFreeMmsLocationPosition() throws IOException {
    synchronized (fsLock) {
      long freeMmsLocPos = 0, nextPagePos;
      JefsPageHeader header = null;

      nextPagePos = getFirstJefsMmsPagePosition();

      while (nextPagePos < fsRaf.length()) {
        header = readPageHeader(nextPagePos);

        if (header.getObjectType() == JefsObjectType.MEMORY_MANAGEMENT_SEGMENT) {
          freeMmsLocPos = findFreeMmsLocationPosition(header);

          if (freeMmsLocPos >= 0) {
            break;
          }
        }

        if (header.getNextPagePosition() == 0) {
          break;
        }
        else {
          nextPagePos = header.getNextPagePosition();
        }
      }

      if (freeMmsLocPos == 0) {
        freeMmsLocPos = allocateNewMemoryManagementSegment(header);
      }

      return freeMmsLocPos;
    }
  }

  @SuppressWarnings("unused")
  private long findFreeMmsLocationPosition(JefsPageHeader mmsHeader) throws IOException {
    synchronized (fsLock) {
      long freeMmsLocPos, mmsLoc, objPos, objId;
      JefsPage rootPage;
      ByteBuffer buf;

      rootPage = readPageByPosition(mmsHeader.getPosition());

      buf = ByteBuffer.wrap(rootPage.getData());

      objPos = 0;
      mmsLoc = 0;

      while (mmsLoc < fsInfo.getPageDataSize()) {
        objPos = buf.getLong();
        objId = buf.getLong();

        if (objPos == 0) {
          break;
        }

        mmsLoc += MMS_PAGE_ENTRY_LEN;
      }

      if (mmsLoc < fsInfo.getPageDataSize() && objPos == 0) {
        freeMmsLocPos = mmsHeader.getPosition() + fsInfo.getPageHeaderSize() + mmsLoc;
      }
      else {
        freeMmsLocPos = -1;
      }

      buf = null;
      rootPage = null;

      return freeMmsLocPos;
    }
  }

  @SuppressWarnings("unused")
  private long findMmsLocationPosition(long mmsPagePosition) throws IOException {
    long mmsLocPos, nextPagePos, objPos, objId, mmsPosInFile = 0;
    JefsPage mmsPage;
    ByteBuffer buf;

    nextPagePos = getFirstJefsMmsPagePosition();

    while (nextPagePos > 0) {
      mmsPage = readPageByPosition(nextPagePos);

      buf = ByteBuffer.wrap(mmsPage.getData());

      objPos = 0;
      mmsLocPos = 0;

      while (mmsLocPos < fsInfo.getPageDataSize()) {
        objPos = buf.getLong();
        objId = buf.getLong();

        if (objPos == mmsPagePosition) {
          mmsPosInFile = mmsPage.getPosition() + fsInfo.getPageHeaderSize() + mmsLocPos;
          break;
        }

        mmsLocPos += MMS_PAGE_ENTRY_LEN;
      }

      if (mmsPosInFile > 0) {
        break;
      }

      nextPagePos = mmsPage.getNextPagePosition();
    }

    return mmsPosInFile;
  }

  // END Object Search Operations-------->

  // START Utility Functions------>

  // END Utility Functions-------->

  public static void main(String[] args) {
    JefsHeapFileSystem hfs = null;
    String jefsVolumeFilePath;
    int exitCd;

    if (args.length != 1) {
      exitCd = 1;
      System.err.println("Usage: java " + JefsHeapFileSystem.class.getName() + " [JEFS_VOLUME_FILE_PATH]");
    }
    else {
      try {
        jefsVolumeFilePath = args[0];
        jefsVolumeFilePath = jefsVolumeFilePath.trim();

        hfs = new JefsHeapFileSystem(jefsVolumeFilePath);

        hfs.format();

        hfs.open();

        hfs.printFileSystemInfo();

        hfs.updateVolumeName("TestFileHeapMemory");

        hfs.printFileSystemInfo();

        hfs.printFreeChainInfo();

        exitCd = 0;
      } // End try block
      catch (Exception e) {
        exitCd = 1;
        e.printStackTrace();
      }
      finally {
        try {
          if (hfs != null) {
            hfs.close();
            hfs = null;
          }
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    System.exit(exitCd);
  }

}
