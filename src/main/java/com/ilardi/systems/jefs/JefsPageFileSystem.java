/**
 * Created Feb 25, 2021
 */
package com.ilardi.systems.jefs;

import static com.ilardi.systems.jefs.JefsConstants.JEFS_MAGIC_BYTES;
import static com.ilardi.systems.jefs.JefsConstants.JEFS_ROOT_PAT_NAME;
import static com.ilardi.systems.jefs.JefsConstants.JEFS_VERSION;
import static com.ilardi.systems.jefs.JefsConstants.PAT_PAGE_ENTRY_LEN;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author robert.ilardi
 *
 */

public class JefsPageFileSystem extends JefsRawFileSystem {

  public JefsPageFileSystem(String magicBytes, int version, String physicalFilePath, String volumeName) throws IOException {
    super(magicBytes, version, physicalFilePath, volumeName);
  }

  // START Public High Level Operations------>

  @Override
  public long getFileSystemFirstActiveDataPagePosition() throws IOException {
    return getFirstPageFsDataPagePosition();
  }

  /*
   * @Override public List<Long> getRootPagePositions() throws IOException {
   * synchronized (fsLock) { ArrayList<Long> rootPagePositions; JefsPage patPage;
   * long position;
   * 
   * position = getFirstPageFsPatPagePosition();
   * 
   * rootPagePositions = new ArrayList<Long>();
   * 
   * while (position > 0) { patPage = readPageByPosition(position);
   * 
   * readAllRootPagePositions(patPage, rootPagePositions);
   * 
   * position = patPage.getNextPagePosition(); }
   * 
   * return rootPagePositions; } }
   */

  @Override
  public long getStartingRootPagePosition() throws IOException {
    return getFirstPageFsPatPagePosition();
  }

  @Override
  public List<Long> getNextRootPagePositions(long startingRootPagePos) throws IOException {
    synchronized (fsLock) {
      ArrayList<Long> rootPagePositions;
      JefsPage patPage;
      long position;

      position = startingRootPagePos;

      rootPagePositions = new ArrayList<Long>();

      if (position > 0) {
        patPage = readPageByPosition(position);

        readAllRootPagePositions(patPage, rootPagePositions);

        position = patPage.getNextPagePosition();

        rootPagePositions.add(0, position); // Add Next Root Page Position
      }

      return rootPagePositions;
    }
  }

  // END Public High Level Operations-------->

  // START File System Init Functions------>

  protected void initNewFileSystem() throws IOException {
    synchronized (fsLock) {
      super.initNewFileSystem();
      createRootPageAllocationTable();
    }
  }

  private void createRootPageAllocationTable() throws IOException {
    synchronized (fsLock) {
      allocatePage(0, 0, JEFS_ROOT_PAT_NAME, JefsObjectType.PAGE_ALLOCATION_TABLE, 0, null);
    }
  }

  // END File System Init Functions-------->

  // START Page Level I/O Operations------>

  protected void deletePage(JefsPage page) throws IOException {
    synchronized (fsLock) {
      if (page == null) {
        throw new IOException("Cannot Perform Delete Operation on NULL Page!");
      }

      deletePage(page.getHeader(), page.getData());
    }
  }

  protected void deletePage(JefsPageHeader header, byte[] pageData) throws IOException {
    synchronized (fsLock) {
      if (header == null) {
        throw new IOException("Cannot Perform Delete Operation on NULL Header!");
      }

      if (header.getObjectType() != JefsObjectType.PAGE_ALLOCATION_TABLE && header.getParentObjectId() == 0) {
        removeRootPageAllocationTableEntry(header.getPosition());
      }

      super.deletePage(header, pageData);
    }
  }

  protected void writePage(JefsPageHeader pageHeader, byte[] pageData) throws IOException {
    synchronized (fsLock) {
      if (pageHeader.getObjectType() != JefsObjectType.PAGE_ALLOCATION_TABLE && pageHeader.getParentObjectId() == 0 && pageHeader.getStatusCode() != JefsStatusCodes.DELETED) {
        insertRootPageAllocationTableEntry(pageHeader.getPosition(), pageHeader.getObjectId());
      }

      super.writePage(pageHeader, pageData);
    }
  }

  private void insertRootPageAllocationTableEntry(long rootPagePosition, long rootPageObjectId) throws IOException {
    synchronized (fsLock) {
      byte[] bArr;
      ByteBuffer buf;
      long patPos;

      patPos = findPatLocationPosition(rootPagePosition);

      if (patPos > 0) {
        return;
      }

      patPos = findFreePatLocationPosition();

      bArr = new byte[PAT_PAGE_ENTRY_LEN];

      fillNulls(bArr);

      buf = ByteBuffer.wrap(bArr);

      buf.putLong(rootPagePosition);
      buf.putLong(rootPageObjectId);

      writeData(bArr, patPos, true);

      buf = null;
      bArr = null;
    }
  }

  private void removeRootPageAllocationTableEntry(long rootPagePosition) throws IOException {
    synchronized (fsLock) {
      byte[] bArr;
      long patPos;

      patPos = findPatLocationPosition(rootPagePosition);

      if (patPos > 0) {
        bArr = new byte[PAT_PAGE_ENTRY_LEN];

        fillNulls(bArr);

        writeData(bArr, patPos, true);

        bArr = null;
      }
    }
  }

  private long allocateNewRootPageAllocationTable(JefsPageHeader lastRootPatHeader) throws IOException {
    synchronized (fsLock) {
      long freePatLocPos;
      JefsPageHeader rootHeader;
      JefsPage rootPage;

      rootPage = allocatePage(lastRootPatHeader.getParentObjectId(), lastRootPatHeader.getParentPagePosition(), JEFS_ROOT_PAT_NAME, JefsObjectType.PAGE_ALLOCATION_TABLE,
          lastRootPatHeader.getObjectPageIndex() + 1, null);

      rootHeader = rootPage.getHeader();

      freePatLocPos = rootHeader.getPosition() + rootHeader.getPageHeaderSize();

      return freePatLocPos;
    }
  }

  @SuppressWarnings("unused")
  @Override
  protected void readAllRootPagePositions(JefsPage patPage, ArrayList<Long> rootPagePositions) throws IOException {
    synchronized (fsLock) {
      long patLoc, objPos, objId;
      ByteBuffer buf;

      buf = ByteBuffer.wrap(patPage.getData());

      objPos = 0;
      patLoc = 0;

      while (patLoc < fsInfo.getPageDataSize()) {
        objPos = buf.getLong();
        objId = buf.getLong();

        /*
         * if (objPos == 0) { break; } else { rootPagePositions.add(objPos); patLoc +=
         * PAT_PAGE_ENTRY_LEN; }
         */

        if (objPos != 0) {
          rootPagePositions.add(objPos);
        }

        patLoc += PAT_PAGE_ENTRY_LEN;
      }

      buf = null;
    }
  }

  // END Page Level I/O Operations-------->

  // START Object Search Operations------>

  protected long getFirstPageFsDataPagePosition() throws IOException {
    synchronized (fsLock) {
      long objectFirstPagePosition;

      objectFirstPagePosition = getFirstPageFsPatPagePosition();
      objectFirstPagePosition += fsInfo.getTotalPageSize();

      return objectFirstPagePosition;
    }
  }

  protected long getFirstPageFsPatPagePosition() throws IOException {
    synchronized (fsLock) {
      long objectFirstPagePosition;

      objectFirstPagePosition = getFirstRawFsDataPagePosition();

      return objectFirstPagePosition;
    }
  }

  private long findFreePatLocationPosition() throws IOException {
    synchronized (fsLock) {
      long freePatLocPos = 0, nextPagePos;
      JefsPageHeader header = null;

      nextPagePos = getFirstPageFsPatPagePosition();

      while (nextPagePos < fsRaf.length()) {
        header = readPageHeader(nextPagePos);

        if (header.getObjectType() == JefsObjectType.PAGE_ALLOCATION_TABLE) {
          freePatLocPos = findFreePatLocationPosition(header);

          if (freePatLocPos >= 0) {
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

      if (freePatLocPos == 0) {
        freePatLocPos = allocateNewRootPageAllocationTable(header);
      }

      return freePatLocPos;
    }
  }

  @SuppressWarnings("unused")
  private long findFreePatLocationPosition(JefsPageHeader rootPatHeader) throws IOException {
    synchronized (fsLock) {
      long freePatLocPos, patLoc, objPos, objId;
      JefsPage rootPage;
      ByteBuffer buf;

      rootPage = readPageByPosition(rootPatHeader.getPosition());

      buf = ByteBuffer.wrap(rootPage.getData());

      objPos = 0;
      patLoc = 0;

      while (patLoc < fsInfo.getPageDataSize()) {
        objPos = buf.getLong();
        objId = buf.getLong();

        if (objPos == 0) {
          break;
        }

        patLoc += PAT_PAGE_ENTRY_LEN;
      }

      if (patLoc < fsInfo.getPageDataSize() && objPos == 0) {
        freePatLocPos = rootPatHeader.getPosition() + fsInfo.getPageHeaderSize() + patLoc;
      }
      else {
        freePatLocPos = -1;
      }

      buf = null;
      rootPage = null;

      return freePatLocPos;
    }
  }

  @SuppressWarnings("unused")
  private long findPatLocationPosition(long rootPagePosition) throws IOException {
    long patLocPos, nextPagePos, objPos, objId, patPosInFile = 0;
    JefsPage patPage;
    ByteBuffer buf;

    nextPagePos = getFirstPageFsPatPagePosition();

    while (nextPagePos > 0) {
      patPage = readPageByPosition(nextPagePos);

      buf = ByteBuffer.wrap(patPage.getData());

      objPos = 0;
      patLocPos = 0;

      while (patLocPos < fsInfo.getPageDataSize()) {
        objPos = buf.getLong();
        objId = buf.getLong();

        if (objPos == rootPagePosition) {
          patPosInFile = patPage.getPosition() + fsInfo.getPageHeaderSize() + patLocPos;
          break;
        }

        patLocPos += PAT_PAGE_ENTRY_LEN;
      }

      if (patPosInFile > 0) {
        break;
      }

      nextPagePos = patPage.getNextPagePosition();
    }

    return patPosInFile;
  }

  // END Object Search Operations-------->

  public static void main(String[] args) {
    JefsPageFileSystem pgfs = null;
    String pageVolumeFilePath, objectName;
    int exitCd;
    JefsPage page;
    long pagePos;
    JefsPageHeader header;
    byte[] pageData;

    if (args.length != 1) {
      exitCd = 1;
      System.err.println("Usage: java " + JefsPageFileSystem.class.getName() + " [PAGE_FS_VOLUME_FILE_PATH]");
    }
    else {
      try {
        pageVolumeFilePath = args[0];
        pageVolumeFilePath = pageVolumeFilePath.trim();

        pgfs = new JefsPageFileSystem(JEFS_MAGIC_BYTES, JEFS_VERSION, pageVolumeFilePath, null);

        pgfs.format();

        pgfs.open();

        pgfs.printFileSystemInfo();

        pgfs.updateVolumeName("Robert's Page File System Volume");

        objectName = LocalDateTime.now().toString();
        pageData = pgfs.generateRandomData(pgfs.getMaxPageDataLength(), false);
        page = pgfs.allocatePage(0, 0, objectName, JefsObjectType.DATAPAGE, 0, pageData);

        for (int i = 0; i < 10; i++) {
          header = page.getHeader();
          objectName = LocalDateTime.now().toString();
          pageData = pgfs.generateRandomData(pgfs.getMaxPageDataLength(), false);
          page = pgfs.allocatePage(header.getObjectId(), header.getPosition(), objectName, JefsObjectType.DATAPAGE, header.getObjectPageIndex() + 1, pageData);
        }

        pgfs.printPageChainInfo();

        System.out.println("Total Pages: " + pgfs.countPages());
        System.out.println("Active Pages: " + pgfs.countActiveDataPages());

        pageData = pgfs.generateRandomData(10240, true);

        page = pgfs.allocatePage(0, 0, "BigFile.dat", JefsObjectType.DATAPAGE, 0, null);

        System.out.println(page);

        page = pgfs.appendDataToPageChain(page, pageData);

        System.out.println(page);

        header = pgfs.findRootPagesByObjectName("BigFile.dat").get(0);
        page = pgfs.readPage(header);

        System.out.println(page);

        pageData = pgfs.generateRandomData(10240, true);
        page = pgfs.appendDataToPageChain(page, pageData);

        System.out.println(page);

        System.out.println("Total Pages: " + pgfs.countPages());
        System.out.println("Active Pages: " + pgfs.countActiveDataPages());

        pgfs.printPageChainInfo();

        pagePos = pgfs.getFileSystemFirstActiveDataPagePosition();

        page = pgfs.readPageByPosition(pagePos);

        pgfs.deletePageChain(page);

        pgfs.printFileSystemInfo();

        pgfs.printPageChainInfo();

        System.out.println("Total Pages: " + pgfs.countPages());
        System.out.println("Active Pages: " + pgfs.countActiveDataPages());

        pgfs.printFreeChainInfo();

        // Finish Tests Above This Line-------------------->

        pgfs.printFileSystemInfo();

        exitCd = 0;
      } // End try block
      catch (Exception e) {
        exitCd = 1;
        e.printStackTrace();
      }
      finally {
        try {
          if (pgfs != null) {
            pgfs.close();
            pgfs = null;
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
