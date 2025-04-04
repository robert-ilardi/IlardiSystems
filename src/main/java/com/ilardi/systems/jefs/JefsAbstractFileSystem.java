/**
 * Created Mar 5, 2021
 */
package com.ilardi.systems.jefs;

import static com.ilardi.systems.jefs.JefsConstants.DEFAULT_PAGE_DATA_SIZE;
import static com.ilardi.systems.jefs.JefsConstants.DEFAULT_PAGE_HEADER_SIZE;
import static com.ilardi.systems.jefs.JefsConstants.DEFAULT_VOLUME_NAME;
import static com.ilardi.systems.jefs.JefsConstants.DOS_PATH_SEPARATOR_REGEX;
import static com.ilardi.systems.jefs.JefsConstants.FIRST_OBJECT_ID;
import static com.ilardi.systems.jefs.JefsConstants.FIRST_OBJECT_POS;
import static com.ilardi.systems.jefs.JefsConstants.JEFS_MAGIC_BYTES;
import static com.ilardi.systems.jefs.JefsConstants.JEFS_PATH_SEPARATOR;
import static com.ilardi.systems.jefs.JefsConstants.JEFS_PATH_SEPARATOR_REGEX;
import static com.ilardi.systems.jefs.JefsConstants.JEFS_ROOT_DIRECTORY;
import static com.ilardi.systems.jefs.JefsConstants.JEFS_VERSION;
import static com.ilardi.systems.jefs.JefsConstants.PAGE_CREATED_TS_OFFSET;
import static com.ilardi.systems.jefs.JefsConstants.PAGE_MODIFIED_TS_OFFSET;
import static com.ilardi.systems.jefs.JefsConstants.PAGE_NEXT_PAGE_POS_OFFSET;
import static com.ilardi.systems.jefs.JefsConstants.PAGE_OBJECT_ID_OFFSET;
import static com.ilardi.systems.jefs.JefsConstants.PAGE_OBJECT_NAME_LEN;
import static com.ilardi.systems.jefs.JefsConstants.PAGE_OBJECT_NANE_OFFSET;
import static com.ilardi.systems.jefs.JefsConstants.PAGE_OBJECT_PAGE_INDEX_OFFSET;
import static com.ilardi.systems.jefs.JefsConstants.PAGE_OBJECT_STATUS_LEN;
import static com.ilardi.systems.jefs.JefsConstants.PAGE_OBJECT_STATUS_OFFSET;
import static com.ilardi.systems.jefs.JefsConstants.PAGE_OBJECT_TYPE_LEN;
import static com.ilardi.systems.jefs.JefsConstants.PAGE_OBJECT_TYPE_OFFSET;
import static com.ilardi.systems.jefs.JefsConstants.PAGE_PARENT_OBJECT_ID_OFFSET;
import static com.ilardi.systems.jefs.JefsConstants.PAGE_PARENT_PAGE_POS_OFFSET;
import static com.ilardi.systems.jefs.JefsConstants.PAGE_USED_DATA_SIZE_OFFSET;
import static com.ilardi.systems.jefs.JefsConstants.VOLUME_HEADER_SIZE;
import static com.ilardi.systems.jefs.JefsConstants.VOL_CREATION_TS_POS;
import static com.ilardi.systems.jefs.JefsConstants.VOL_FREE_CHAIN_BEGIN_POS;
import static com.ilardi.systems.jefs.JefsConstants.VOL_FREE_CHAIN_END_POS;
import static com.ilardi.systems.jefs.JefsConstants.VOL_MAGIC_BYTES_LEN;
import static com.ilardi.systems.jefs.JefsConstants.VOL_MAGIC_BYTES_POS;
import static com.ilardi.systems.jefs.JefsConstants.VOL_MODIFICATION_TS_LEN;
import static com.ilardi.systems.jefs.JefsConstants.VOL_MODIFICATION_TS_POS;
import static com.ilardi.systems.jefs.JefsConstants.VOL_NEXT_OBJECT_ID_POS;
import static com.ilardi.systems.jefs.JefsConstants.VOL_PAGE_DATA_SIZE_POS;
import static com.ilardi.systems.jefs.JefsConstants.VOL_PAGE_HEADER_SIZE_POS;
import static com.ilardi.systems.jefs.JefsConstants.VOL_VERSION_POS;
import static com.ilardi.systems.jefs.JefsConstants.VOL_VOLUME_NAME_LEN;
import static com.ilardi.systems.jefs.JefsConstants.VOL_VOLUME_NAME_POS;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author robert.ilardi
 *
 */

public abstract class JefsAbstractFileSystem {

  protected JefsFileSystemInfo fsInfo;

  protected final String physicalFilePath;
  protected final String magicBytes;
  protected final int version;

  protected String volumeName;

  protected final Object fsLock;

  protected PhysicalRandomAccessFile fsRaf;

  public JefsAbstractFileSystem(String magicBytes, int version, String physicalFilePath, String volumeName) throws IOException {
    if (magicBytes == null) {
      throw new IOException("Magic Bytes Cannot be NULL!");
    }

    if (magicBytes.trim().length() != VOL_MAGIC_BYTES_LEN) {
      throw new IOException("Magic Bytes Length is Invalid!");
    }

    this.magicBytes = magicBytes.trim();

    this.version = version;

    this.physicalFilePath = physicalFilePath;
    this.volumeName = volumeName;

    fsLock = new Object();

    fsRaf = null;
  }

  // START Public High Level Operations------>

  public boolean exists() {
    synchronized (fsLock) {
      File f = new File(physicalFilePath);

      return f.exists();
    }
  }

  public void open() throws IOException {
    synchronized (fsLock) {
      if (fsRaf == null) {
        fsRaf = openRaf();

        initFileSystem();
      }
    }
  }

  protected PhysicalRandomAccessFile openRaf() throws IOException {
    synchronized (fsLock) {
      PhysicalRandomAccessFile iFsRaf;

      iFsRaf = new PhysicalRandomAccessFile(physicalFilePath, "rw");

      return iFsRaf;
    }
  }

  public void close() throws IOException {
    synchronized (fsLock) {
      if (fsRaf != null) {
        try {
          fsRaf.close();
        }
        finally {
          fsRaf = null;
        }
      }
    }
  }

  public void printFileSystemInfo() {
    System.out.println(fsInfo);
  }

  public String getVolumeName() {
    return (fsInfo != null ? fsInfo.getVolumeName() : "[NULL]");
  }

  public String getPhysicalFileName() {
    return (fsInfo != null ? fsInfo.getPhysicalFilePath() : "[NULL]");
  }

  public String getFileSystemInfoString() {
    return (fsInfo != null ? fsInfo.toString() : "[NULL]");
  }

  public int getFileSystemInfoPageDataSize() {
    return (fsInfo != null ? fsInfo.getPageDataSize() : -1);
  }

  public void format() throws IOException {
    synchronized (fsLock) {
      if (fsRaf != null) {
        fsRaf.close();
      }

      fsRaf = openRaf();

      fsRaf.setLength(0);

      initFileSystem();
    }
  }

  public long countPages() throws IOException {
    synchronized (fsLock) {
      JefsPageHeader header;
      long position, pageCnt = 0;

      position = getFirstRawPagePosition();

      while (position < fsRaf.length()) {
        header = readPageHeader(position);

        if (header != null) {
          pageCnt++;
        }

        position += fsInfo.getTotalPageSize();
      }

      return pageCnt;
    }
  }

  public long countActivePages() throws IOException {
    synchronized (fsLock) {
      JefsPageHeader header;
      long position, pageCnt = 0;

      position = getFirstRawPagePosition();

      while (position < fsRaf.length()) {
        header = readPageHeader(position);

        if (header != null && header.getStatusCode() == JefsStatusCodes.ACTIVE) {
          pageCnt++;
        }

        position += fsInfo.getTotalPageSize();
      }

      return pageCnt;
    }
  }

  public abstract long getFileSystemFirstActiveDataPagePosition() throws IOException;

  public long countActiveDataPages() throws IOException {
    synchronized (fsLock) {
      JefsPageHeader header;
      long position, pageCnt = 0;

      position = getFileSystemFirstActiveDataPagePosition();

      if (position > 0) {
        while (position < fsRaf.length()) {
          header = readPageHeader(position);

          if (header != null && header.getStatusCode() == JefsStatusCodes.ACTIVE && isDataPage(header)) {
            pageCnt++;
          }

          position += fsInfo.getTotalPageSize();
        }
      }

      return pageCnt;
    }
  }

  protected boolean isDataPage(JefsPageHeader header) {
    return (header.getObjectType() == JefsObjectType.DATAPAGE || header.getObjectType() == JefsObjectType.DIRECTORY || header.getObjectType() == JefsObjectType.FILE
        || header.getObjectType() == JefsObjectType.VARIABLE);
  }

  public void updateVolumeName(String volumeNewName) throws IOException {
    synchronized (fsLock) {
      long ts;
      byte[] bTmp;

      ts = System.currentTimeMillis();

      if (volumeNewName == null) {
        return;
      }

      volumeNewName = volumeNewName.trim();

      bTmp = volumeNewName.getBytes();

      if (bTmp.length > VOL_VOLUME_NAME_LEN) {
        bTmp = cutToLen(bTmp, VOL_VOLUME_NAME_LEN);

        volumeNewName = new String(bTmp);

        volumeNewName = volumeNewName.trim();

        bTmp = volumeNewName.getBytes();
      }

      if (this.volumeName.equals(volumeNewName)) {
        return;
      }

      bTmp = null;
      this.volumeName = volumeNewName;
      fsInfo.setVolumeName(this.volumeName);
      fsInfo.setModificationTs(ts);

      writeFileSystemInfo();
    }
  }

  public void writePage(JefsPage page) throws IOException {
    synchronized (fsLock) {
      if (page != null) {
        writePage(page.getHeader(), page.getData());
      }
    }
  }

  public JefsPage allocatePage(long parentObjectId, long parentPagePosition, String objectName, JefsObjectType objectType, long objectPageIndex, byte[] data) throws IOException {
    synchronized (fsLock) {
      JefsPageHeader header;
      JefsPage page;
      long objectPosition, objectId, ts;

      if (objectName != null && objectName.trim().length() > PAGE_OBJECT_NAME_LEN) {
        throw new IOException("Object Name '" + objectName.trim() + "' exceeds max allowed length of " + PAGE_OBJECT_NAME_LEN);
      }

      if (data != null && data.length > fsInfo.getPageDataSize()) {
        throw new IOException("Object Data Size of " + data.length + " exceeds max allowed length of " + fsInfo.getPageDataSize());
      }

      ts = System.currentTimeMillis();

      objectId = getNextObjectId();
      objectPosition = getNextObjectPosition();

      header = new JefsPageHeader(objectId, objectPosition, ts, parentObjectId, (objectName != null ? objectName.trim() : null), objectType, JefsStatusCodes.ACTIVE, ts, fsInfo.getPageHeaderSize(),
          fsInfo.getPageDataSize(), objectPageIndex, (data != null ? data.length : 0), 0, parentPagePosition);

      page = new JefsPage(header, data);

      writePage(page);

      fsInfo.setModificationTs(ts);

      writeFileSystemInfo();

      return page;
    }
  }

  public void deletePageChain(JefsPage page) throws IOException {
    synchronized (fsLock) {
      JefsPage tailPage;
      JefsPageHeader header, tailHeader;
      long parentPagePos;
      byte[] data;

      if (page != null) {
        header = page.getHeader();

        if (header != null) {
          data = page.getData();

          tailHeader = getLastPageHeader(header);

          parentPagePos = tailHeader.getParentPagePosition();

          deletePage(tailHeader, data);

          while (parentPagePos != 0) {
            // tailHeader = readPageHeader(parentPagePos);
            tailPage = readPageByPosition(parentPagePos);
            tailHeader = tailPage.getHeader();
            data = tailPage.getData();

            parentPagePos = tailHeader.getParentPagePosition();

            deletePage(tailHeader, data);
          }
        }
      }
    }
  }

  public void deletePageChainUsingPageIndex(JefsPage page) throws IOException {
    synchronized (fsLock) {
      JefsPage tailPage;
      JefsPageHeader header, tailHeader;
      long parentPagePos, pageIndex;
      byte[] data;

      if (page != null) {
        header = page.getHeader();

        if (header != null) {
          data = page.getData();

          tailHeader = getLastPageHeader(header);

          parentPagePos = tailHeader.getParentPagePosition();
          pageIndex = tailHeader.getObjectPageIndex();

          deletePage(tailHeader, data);

          while (parentPagePos != 0 && pageIndex != 0) {
            // tailHeader = readPageHeader(parentPagePos);
            tailPage = readPageByPosition(parentPagePos);

            tailHeader = tailPage.getHeader();
            data = tailPage.getData();

            parentPagePos = tailHeader.getParentPagePosition();
            pageIndex = tailHeader.getObjectPageIndex();

            deletePage(tailHeader, data);
          }
        }
      }
    }
  }

  public boolean pageExists(long objectId) throws IOException {
    synchronized (fsLock) {
      long position;
      boolean exists;

      position = findObjectFirstPagePosition(objectId);

      exists = (position > 0);

      return exists;
    }
  }

  public JefsPage readPage(JefsPageHeader header) throws IOException {
    synchronized (fsLock) {
      JefsPage page = null;
      byte[] data;

      if (header != null) {
        data = readPageData(header);
        page = new JefsPage(header, data);
      }

      return page;
    }
  }

  public JefsPage readPageByPosition(long position) throws IOException {
    synchronized (fsLock) {
      JefsPageHeader header;
      JefsPage page;

      if (position == 0) {
        position = getFirstRawPagePosition();
      }

      header = readPageHeader(position);

      page = readPage(header);

      return page;
    }
  }

  public void rename(JefsPage page, String newName) throws IOException {
    synchronized (fsLock) {
      rename(page.getHeader(), newName);
    }
  }

  public void rename(JefsPageHeader header, String newName) throws IOException {
    synchronized (fsLock) {
      rename(header.getPosition(), newName);
    }
  }

  public void rename(long position, String newName) throws IOException {
    synchronized (fsLock) {
      JefsPageHeader origHeader;
      JefsPage page;

      origHeader = readPageHeader(position);

      if (origHeader != null && (origHeader.getObjectName() == null || !origHeader.getObjectName().equals(newName))) {
        page = readPage(origHeader);

        page.getHeader().setObjectName(newName);

        writePage(page);
      }
    }
  }

  protected abstract void readAllRootPagePositions(JefsPage jefsPage, ArrayList<Long> rootPagePositions) throws IOException;

  /*
   * public List<Long> getRootPagePositions() throws IOException { synchronized
   * (fsLock) { ArrayList<Long> rootPagePositions; JefsPageHeader header; long
   * position, nextPagePos;
   * 
   * rootPagePositions = new ArrayList<Long>();
   * 
   * nextPagePos = getFirstRawPagePosition();
   * 
   * while (nextPagePos < fsRaf.length()) { header = readPageHeader(nextPagePos);
   * 
   * if (header.getParentObjectId() == 0) { position = header.getPosition();
   * 
   * rootPagePositions.add(position); }
   * 
   * nextPagePos += fsInfo.getTotalPageSize(); }
   * 
   * return rootPagePositions; } }
   */

  /*
   * public void printPageChainInfo() throws IOException { synchronized (fsLock) {
   * List<Long> rootPagePositions; JefsPageHeader header; long position;
   * 
   * rootPagePositions = getRootPagePositions();
   * 
   * if (rootPagePositions != null) { for (int i = 0; i <
   * rootPagePositions.size(); i++) { position = rootPagePositions.get(i); header
   * = readPageHeader(position);
   * 
   * System.out.println("Page Chain Root at: " + position);
   * 
   * printPageChainInfo(header); } } } }
   */

  public void printPageChainInfo() throws IOException {
    synchronized (fsLock) {
      List<Long> rootPagePositions;
      JefsPageHeader header;
      long position, startingRootPagePos;

      startingRootPagePos = getStartingRootPagePosition();

      while (startingRootPagePos > 0) {
        rootPagePositions = getNextRootPagePositions(startingRootPagePos);

        // Advance to Next Starting Root Page Position
        startingRootPagePos = rootPagePositions.remove(0);

        if (rootPagePositions != null) {
          for (int i = 0; i < rootPagePositions.size(); i++) {
            position = rootPagePositions.get(i);
            header = readPageHeader(position);

            System.out.println("Page Chain Root at: " + position);

            printPageChainInfo(header);
          }
        }
      }
    }
  }

  public void printFreeChainInfo() throws IOException {
    synchronized (fsLock) {
      JefsPageHeader header;
      long position;

      position = fsInfo.getFreeChainBeginPosition();

      if (position < fsRaf.length()) {
        System.out.println("Free Chain Root at: " + position);

        while (position > 0) {
          header = readPageHeader(position);

          System.out.println(header);

          position = header.getNextPagePosition();
        }
      }
      else {
        System.out.println("End of File Free Chain: " + position);
      }
    }
  }

  public int getMaxPageDataLength() {
    return fsInfo.getPageDataSize();
  }

  public void appendDataToPage(JefsPage page, byte[] additionalPageData) throws IOException {
    synchronized (fsLock) {
      JefsPageHeader header;
      int totalDataLen, oldDataLen, additionalDataLen;
      byte[] oldPageData, combinedPageData;

      if (additionalPageData == null) {
        return;
      }

      if (page == null) {
        return;
      }

      header = page.getHeader();
      additionalDataLen = additionalPageData.length;
      oldPageData = page.getUsedDataOnly();

      oldDataLen = (oldPageData != null ? oldPageData.length : 0);
      totalDataLen = oldDataLen + additionalDataLen;

      if (totalDataLen > fsInfo.getPageDataSize()) {
        throw new IOException("Object Data Size of " + totalDataLen + " exceeds max allowed length of " + fsInfo.getPageDataSize());
      }

      if (oldPageData != null) {
        combinedPageData = new byte[totalDataLen];

        System.arraycopy(oldPageData, 0, combinedPageData, 0, oldDataLen);
        System.arraycopy(additionalPageData, 0, combinedPageData, oldDataLen, additionalDataLen);
      }
      else {
        combinedPageData = additionalPageData;
      }

      page.setData(combinedPageData);
      header.setUsedDataSize(totalDataLen);

      writePage(page);
    }
  }

  public JefsPage appendDataToPageChain(JefsPageHeader tailHeader, byte[] additionalData) throws IOException {
    synchronized (fsLock) {
      JefsPage tailPage, newTailPage;

      if (additionalData == null) {
        return null;
      }

      if (tailHeader == null) {
        return null;
      }

      tailPage = readPage(tailHeader);

      newTailPage = appendDataToPageChain(tailPage, additionalData);

      return newTailPage;
    }
  }

  public PageChainAnchors createNewPageChain(String objName, JefsObjectType objType, byte[] chainData) throws IOException {
    synchronized (fsLock) {
      PageChainAnchors anchors;
      JefsPage headPage, tailPage;

      headPage = allocatePage(0, 0, objName, objType, 0, null);

      tailPage = appendDataToPageChain(headPage, chainData);

      anchors = new PageChainAnchors(headPage, tailPage);

      return anchors;
    }
  }

  /*
   * public List<JefsPageHeader> findRootPagesByObjectName(String objectName)
   * throws IOException { synchronized (fsLock) { ArrayList<JefsPageHeader>
   * rootPageHeaders; List<Long> rootPagePositions; JefsPageHeader header; long
   * position;
   * 
   * rootPagePositions = getRootPagePositions();
   * 
   * rootPageHeaders = new ArrayList<JefsPageHeader>();
   * 
   * if (rootPagePositions != null) { for (int i = 0; i <
   * rootPagePositions.size(); i++) { position = rootPagePositions.get(i);
   * 
   * header = readPageHeader(position);
   * 
   * if (header.getObjectName() != null &&
   * header.getObjectName().equals(objectName)) { rootPageHeaders.add(header); } }
   * }
   * 
   * return rootPageHeaders; } }
   */

  public long getStartingRootPagePosition() throws IOException {
    return getFirstRawPagePosition();
  }

  /*
   * public List<Long> getNextRootPagePositions(long startingRootPagePos) throws
   * IOException { synchronized (fsLock) { ArrayList<Long> rootPagePositions;
   * JefsPageHeader header; long position, nextPagePos;
   * 
   * //nextPagePos = getFirstRawPagePosition(); nextPagePos = startingRootPagePos;
   * 
   * rootPagePositions = new ArrayList<Long>();
   * 
   * while (nextPagePos < fsRaf.length()) { header = readPageHeader(nextPagePos);
   * 
   * if (header.getParentObjectId() == 0) { position = header.getPosition();
   * 
   * rootPagePositions.add(position); }
   * 
   * nextPagePos += fsInfo.getTotalPageSize(); }
   * 
   * rootPagePositions.add(0, 0L); //Add Next Root Page Position, in this case
   * it's NONE
   * 
   * return rootPagePositions; } }
   */

  public List<Long> getNextRootPagePositions(long startingRootPagePos) throws IOException {
    synchronized (fsLock) {
      ArrayList<Long> rootPagePositions;
      JefsPageHeader header;
      long position, nextPagePos;

      nextPagePos = startingRootPagePos;

      rootPagePositions = new ArrayList<Long>();

      header = readPageHeader(nextPagePos);

      if (header.getParentObjectId() == 0) {
        position = header.getPosition();

        rootPagePositions.add(position);
      }

      nextPagePos += fsInfo.getTotalPageSize();

      if (nextPagePos < fsRaf.length()) {
        // Add Next Root Page Position
        rootPagePositions.add(0, nextPagePos);
      }
      else {
        // Add Next Root Page Position, in this case it's NONE
        rootPagePositions.add(0, 0L);
      }

      return rootPagePositions;
    }
  }

  public List<JefsPageHeader> findRootPagesByObjectName(String objectName) throws IOException {
    synchronized (fsLock) {
      ArrayList<JefsPageHeader> rootPageHeaders;
      List<Long> rootPagePositions;
      JefsPageHeader header;
      long position, startingRootPagePos;

      startingRootPagePos = getStartingRootPagePosition();

      rootPageHeaders = new ArrayList<JefsPageHeader>();

      while (startingRootPagePos > 0) {
        rootPagePositions = getNextRootPagePositions(startingRootPagePos);

        // Advance to Next Starting Root Page Position
        startingRootPagePos = rootPagePositions.remove(0);

        if (rootPagePositions != null) {
          for (int i = 0; i < rootPagePositions.size(); i++) {
            position = rootPagePositions.get(i);

            header = readPageHeader(position);

            if (header.getObjectName() != null && header.getObjectName().equals(objectName)) {
              rootPageHeaders.add(header);
            }
          }
        }
      }

      return rootPageHeaders;
    }
  }

  /*
   * public boolean containsRootPagesNamed(String objectName) throws IOException {
   * synchronized (fsLock) { boolean contains = false; List<Long>
   * rootPagePositions; JefsPageHeader header; long position;
   * 
   * rootPagePositions = getRootPagePositions();
   * 
   * if (rootPagePositions != null) { for (int i = 0; i <
   * rootPagePositions.size(); i++) { position = rootPagePositions.get(i);
   * 
   * header = readPageHeader(position);
   * 
   * if (header.getObjectName() != null &&
   * header.getObjectName().equals(objectName)) { contains = true; break; } } }
   * 
   * return contains; } }
   */

  public boolean containsRootPagesNamed(String objectName) throws IOException {
    synchronized (fsLock) {
      boolean contains = false;
      List<Long> rootPagePositions;
      JefsPageHeader header;
      long position, startingRootPagePos;

      startingRootPagePos = getStartingRootPagePosition();

      while (startingRootPagePos > 0) {
        rootPagePositions = getNextRootPagePositions(startingRootPagePos);

        // Advance to Next Starting Root Page Position
        startingRootPagePos = rootPagePositions.remove(0);

        if (rootPagePositions != null) {
          for (int i = 0; i < rootPagePositions.size(); i++) {
            position = rootPagePositions.get(i);

            header = readPageHeader(position);

            if (header.getObjectName() != null && header.getObjectName().equals(objectName)) {
              contains = true;
              break;
            }
          }
        }
      }

      return contains;
    }
  }

  public void truncatePageChainData(JefsPage page) throws IOException {
    synchronized (fsLock) {
      JefsPage tailPage;
      JefsPageHeader header, tailHeader;
      long parentPagePos;
      byte[] data;

      if (page != null) {
        header = page.getHeader();

        if (header != null) {
          data = page.getData();

          tailHeader = getLastPageHeader(header);

          parentPagePos = tailHeader.getParentPagePosition();

          if (parentPagePos != 0) {
            deletePage(tailHeader, data);
          }
          else {
            truncatePageData(tailHeader);
          }

          while (parentPagePos != 0) {
            tailPage = readPageByPosition(parentPagePos);
            tailHeader = tailPage.getHeader();
            data = tailPage.getData();

            parentPagePos = tailHeader.getParentPagePosition();

            if (parentPagePos != 0) {
              deletePage(tailHeader, data);
            }
            else {
              truncatePageData(tailHeader);
            }
          }
        }
      }
    }
  }

  public void truncatePageChainDataToPage(JefsPage page) throws IOException {
    synchronized (fsLock) {
      JefsPage tailPage;
      JefsPageHeader header, tailHeader;
      long pagePos, tailPagePos, parentPagePos;
      byte[] data;

      if (page != null) {
        header = page.getHeader();

        if (header != null) {
          pagePos = header.getPosition();
          data = page.getData();

          tailHeader = getLastPageHeader(header);

          parentPagePos = tailHeader.getParentPagePosition();
          tailPagePos = tailHeader.getPosition();

          if (tailPagePos != pagePos) {
            deletePage(tailHeader, data);
          }
          else {
            truncatePageData(tailHeader);
          }

          while (tailPagePos != pagePos) {
            tailPage = readPageByPosition(parentPagePos);
            tailHeader = tailPage.getHeader();
            data = tailPage.getData();

            parentPagePos = tailHeader.getParentPagePosition();
            tailPagePos = tailHeader.getPosition();

            if (tailPagePos != pagePos) {
              deletePage(tailHeader, data);
            }
            else {
              truncatePageData(tailHeader);
            }
          }
        }
      }
    }
  }

  // END Public High Level Operations-------->

  // START File System Init Functions------>

  protected void initFileSystem() throws IOException {
    synchronized (fsLock) {
      if (fsRaf.length() == 0) {
        initNewFileSystem();
      }
      else {
        initExistingFileSystem();
      }
    }
  }

  protected void initNewFileSystem() throws IOException {
    synchronized (fsLock) {
      initNewFileSystemHeader();
    }
  }

  protected void initExistingFileSystem() throws IOException {
    synchronized (fsLock) {
      readFileSystem();
    }
  }

  private void initNewFileSystemHeader() throws IOException {
    synchronized (fsLock) {
      initFileSystemHeader();
      writeFileSystemInfo();
    }
  }

  private void initFileSystemHeader() throws IOException {
    synchronized (fsLock) {
      fsInfo = createFileSystemHeader();
      initFileSystemHeader(fsInfo);
    }
  }

  protected JefsFileSystemInfo createFileSystemHeader() throws IOException {
    synchronized (fsLock) {
      final long creationTs = System.currentTimeMillis();
      JefsFileSystemInfo fsInfo;

      fsInfo = new JefsFileSystemInfo(VOLUME_HEADER_SIZE, JEFS_MAGIC_BYTES, JEFS_VERSION, DEFAULT_PAGE_HEADER_SIZE, DEFAULT_PAGE_DATA_SIZE, creationTs, physicalFilePath);

      return fsInfo;
    }
  }

  protected void initFileSystemHeader(JefsFileSystemInfo fsInfo) throws IOException {
    synchronized (fsLock) {
      if (volumeName == null) {
        fsInfo.setVolumeName(DEFAULT_VOLUME_NAME);
      }
      else {
        fsInfo.setVolumeName(this.volumeName.trim());
      }

      this.volumeName = fsInfo.getVolumeName();

      fsInfo.setModificationTs(fsInfo.getCreationTs());

      fsInfo.setNextObjectId(FIRST_OBJECT_ID);
      fsInfo.setFreeChainBeginPosition(VOLUME_HEADER_SIZE);
      fsInfo.setFreeChainEndPosition(VOLUME_HEADER_SIZE);
    }
  }

  private void writeFileSystemInfo() throws IOException {
    synchronized (fsLock) {
      writeFileSystemInfo(fsInfo);
    }
  }

  // END File System Init Functions-------->

  // START Volume I/O Functions------>

  public JefsPage appendDataToPageChain(JefsPage tailPage, byte[] additionalData) throws IOException {
    synchronized (fsLock) {
      JefsPageHeader tailHeader;
      byte[] tailPageData;
      JefsPageHeader newTailHeader = null;
      JefsPage page;
      int oldDataLen, additionalDataLen, curPos, remainingPageLen;
      int availableDataLen, dataChunkLen, pageDataLen;
      byte[] dataChunk;

      if (additionalData == null) {
        return null;
      }

      if (tailPage == null) {
        return null;
      }

      tailHeader = tailPage.getHeader();
      tailPageData = tailPage.getUsedDataOnly();

      if (tailHeader == null) {
        throw new IOException("Append Data Operation Fails on NULL Tail Header!");
      }

      curPos = 0;
      additionalDataLen = additionalData.length;
      availableDataLen = additionalDataLen;

      // Write first partial page---------->

      if (tailPageData != null && tailPageData.length > 0) {
        // Partial Page
        oldDataLen = tailPageData.length;
        remainingPageLen = fsInfo.getPageDataSize() - oldDataLen;

        dataChunkLen = (additionalDataLen >= remainingPageLen ? remainingPageLen : additionalDataLen);
        availableDataLen -= dataChunkLen;

        pageDataLen = oldDataLen + dataChunkLen;

        dataChunk = new byte[pageDataLen];

        System.arraycopy(tailPageData, 0, dataChunk, 0, oldDataLen);
        System.arraycopy(additionalData, curPos, dataChunk, oldDataLen, dataChunkLen);

        curPos += dataChunkLen;

        // newTailHeader = new JefsPageHeader(tailHeader);
        // newTailHeader.setUsedDataSize(pageDataLen);
        tailHeader.setUsedDataSize(pageDataLen);

        // page = new JefsPage(newTailHeader, dataChunk);
        tailPage.setData(dataChunk);

        // writePage(page);
        writePage(tailPage);
      }
      else {
        // Empty Page
        remainingPageLen = fsInfo.getPageDataSize();

        dataChunkLen = (availableDataLen >= remainingPageLen ? remainingPageLen : availableDataLen);
        availableDataLen -= dataChunkLen;

        dataChunk = new byte[dataChunkLen];

        System.arraycopy(additionalData, curPos, dataChunk, 0, dataChunkLen);

        curPos += dataChunkLen;

        // newTailHeader = new JefsPageHeader(tailHeader);
        // newTailHeader.setUsedDataSize(dataChunkLen);
        tailHeader.setUsedDataSize(dataChunkLen);

        // page = new JefsPage(newTailHeader, dataChunk);
        tailPage.setData(dataChunk);

        // writePage(page);
        writePage(tailPage);
      }

      // Write additional pages----------->

      // In case the while loop is not executed
      page = tailPage;

      if (newTailHeader == null) {
        newTailHeader = page.getHeader();
      }

      remainingPageLen = fsInfo.getPageDataSize();

      while (availableDataLen > 0) {
        dataChunkLen = (availableDataLen >= remainingPageLen ? remainingPageLen : availableDataLen);
        availableDataLen -= dataChunkLen;

        dataChunk = new byte[dataChunkLen];

        System.arraycopy(additionalData, curPos, dataChunk, 0, dataChunkLen);

        curPos += dataChunkLen;

        page = allocatePage(newTailHeader.getObjectId(), newTailHeader.getPosition(), newTailHeader.getObjectName(), newTailHeader.getObjectType(), newTailHeader.getObjectPageIndex() + 1, dataChunk);

        newTailHeader = page.getHeader();
      }

      return page;
    }
  }

  private void writeFileSystemInfo(JefsFileSystemInfo fsInfo) throws IOException {
    synchronized (fsLock) {
      byte[] volHeader;
      ByteBuffer buf;

      volHeader = new byte[fsInfo.getVolHeaderSize()];

      // fillNulls(volHeader);

      buf = ByteBuffer.wrap(volHeader);

      writeFileSystemInfoToBuffer(buf);

      writeData(volHeader, VOL_MAGIC_BYTES_POS, false);

      buf = null;
      volHeader = null;
    }
  }

  protected void writeFileSystemInfoToBuffer(ByteBuffer buf) throws IOException {
    synchronized (fsLock) {
      byte[] bTmp;

      bTmp = fsInfo.getJefsMagicBytes().getBytes();
      buf.position(VOL_MAGIC_BYTES_POS);
      buf.put(bTmp);

      buf.position(VOL_VERSION_POS);
      buf.putInt(fsInfo.getJefsVersion());

      buf.position(VOL_PAGE_HEADER_SIZE_POS);
      buf.putInt(fsInfo.getPageHeaderSize());

      buf.position(VOL_PAGE_DATA_SIZE_POS);
      buf.putInt(fsInfo.getPageDataSize());

      bTmp = fsInfo.getVolumeName().getBytes();
      buf.position(VOL_VOLUME_NAME_POS);
      buf.put(bTmp);

      buf.position(VOL_CREATION_TS_POS);
      buf.putLong(fsInfo.getCreationTs());

      buf.position(VOL_MODIFICATION_TS_POS);
      buf.putLong(fsInfo.getModificationTs());

      buf.position(VOL_NEXT_OBJECT_ID_POS);
      buf.putLong(fsInfo.getNextObjectId());

      buf.position(VOL_FREE_CHAIN_BEGIN_POS);
      buf.putLong(fsInfo.getFreeChainBeginPosition());

      buf.position(VOL_FREE_CHAIN_END_POS);
      buf.putLong(fsInfo.getFreeChainEndPosition());
    }
  }

  protected void readFileSystem() throws IOException {
    synchronized (fsLock) {
      readFileSystemHeader();
    }
  }

  private void readFileSystemHeader() throws IOException {
    synchronized (fsLock) {
      byte[] data, bTmp;
      ByteBuffer buf;
      String jefsMagicBytes;
      int jefsVersion, pageHeaderSize, pageDataSize;
      long creationTs, modificationTs, nextObjectId, freeChainBeginPosition, freeChainEndPosition;

      data = readData(VOL_MAGIC_BYTES_POS, VOLUME_HEADER_SIZE);

      buf = ByteBuffer.wrap(data);

      bTmp = new byte[VOL_MAGIC_BYTES_LEN];
      buf.position(VOL_MAGIC_BYTES_POS);
      buf.get(bTmp);
      jefsMagicBytes = new String(bTmp);

      buf.position(VOL_VERSION_POS);
      jefsVersion = buf.getInt();

      buf.position(VOL_PAGE_HEADER_SIZE_POS);
      pageHeaderSize = buf.getInt();

      buf.position(VOL_PAGE_DATA_SIZE_POS);
      pageDataSize = buf.getInt();

      buf.position(VOL_CREATION_TS_POS);
      creationTs = buf.getLong();

      buf.position(VOL_MODIFICATION_TS_POS);
      modificationTs = buf.getLong();

      buf.position(VOL_NEXT_OBJECT_ID_POS);
      nextObjectId = buf.getLong();

      buf.position(VOL_FREE_CHAIN_BEGIN_POS);
      freeChainBeginPosition = buf.getLong();

      buf.position(VOL_FREE_CHAIN_END_POS);
      freeChainEndPosition = buf.getLong();

      bTmp = new byte[VOL_VOLUME_NAME_LEN];
      buf.position(VOL_VOLUME_NAME_POS);
      buf.get(bTmp);
      bTmp = rTrimNull(bTmp);
      this.volumeName = new String(bTmp);

      fsInfo = new JefsFileSystemInfo(VOLUME_HEADER_SIZE, jefsMagicBytes, jefsVersion, pageHeaderSize, pageDataSize, creationTs, physicalFilePath);
      fsInfo.setVolumeName(this.volumeName);
      fsInfo.setModificationTs(modificationTs);
      fsInfo.setNextObjectId(nextObjectId);
      fsInfo.setFreeChainBeginPosition(freeChainBeginPosition);
      fsInfo.setFreeChainEndPosition(freeChainEndPosition);

      bTmp = null;
      buf = null;
      data = null;
    }
  }

  private void updateFileSystemModTs() throws IOException {
    synchronized (fsLock) {
      byte[] bArr = new byte[VOL_MODIFICATION_TS_LEN];
      ByteBuffer buf = ByteBuffer.wrap(bArr);

      fsInfo.setModificationTs(System.currentTimeMillis());
      buf.putLong(fsInfo.getModificationTs());

      fsRaf.seek(VOL_MODIFICATION_TS_POS);
      fsRaf.write(bArr);

      buf = null;
      bArr = null;
    }
  }

  private long getNextObjectId() throws IOException {
    synchronized (fsLock) {
      long objectId, nextObjectId;

      objectId = fsInfo.getNextObjectId();

      nextObjectId = objectId + 1;
      fsInfo.setNextObjectId(nextObjectId);

      return objectId;
    }
  }

  private long getNextObjectPosition() throws IOException {
    synchronized (fsLock) {
      long oldBeginPos, oldEndPos, newBeginPos, newEndPos;
      JefsPageHeader header;
      byte[] headerBytes;

      oldBeginPos = fsInfo.getFreeChainBeginPosition();
      oldEndPos = fsInfo.getFreeChainEndPosition();

      if (oldBeginPos == fsRaf.length()) {
        newBeginPos = oldBeginPos + fsInfo.getTotalPageSize();
        newEndPos = newBeginPos;
      }
      else {
        header = readPageHeader(oldBeginPos);

        newBeginPos = header.getNextPagePosition();

        if (newBeginPos == fsRaf.length()) {
          newEndPos = newBeginPos;
        }
        else if (newBeginPos == 0) {
          newBeginPos = fsRaf.length();
          newEndPos = newBeginPos;
        }
        else {
          header = readPageHeader(newBeginPos);
          header.setParentObjectId(0);
          header.setParentPagePosition(0);

          headerBytes = getPageHeaderBytes(header);
          writeData(headerBytes, header.getPosition(), true);
          headerBytes = null;

          newEndPos = oldEndPos;
        }
      }

      fsInfo.setFreeChainBeginPosition(newBeginPos);
      fsInfo.setFreeChainEndPosition(newEndPos);

      return oldBeginPos;
    }
  }

  // END Volume I/O Functions-------->

  // START Read/Write Base Volume Data Page Operations---->

  protected byte[] getPageHeaderBytes(JefsPageHeader header) {
    byte[] headerBytes, bTmp;
    ByteBuffer buf;

    headerBytes = new byte[header.getPageHeaderSize()];

    // fillNulls(headerBytes);

    buf = ByteBuffer.wrap(headerBytes);

    buf.position(PAGE_OBJECT_ID_OFFSET);
    buf.putLong(header.getObjectId());

    buf.position(PAGE_PARENT_OBJECT_ID_OFFSET);
    buf.putLong(header.getParentObjectId());

    buf.position(PAGE_CREATED_TS_OFFSET);
    buf.putLong(header.getCreatedTs());

    buf.position(PAGE_OBJECT_PAGE_INDEX_OFFSET);
    buf.putLong(header.getObjectPageIndex());

    if (header.getObjectName() != null) {
      bTmp = header.getObjectName().getBytes();
      buf.position(PAGE_OBJECT_NANE_OFFSET);
      buf.put(bTmp);
    }

    bTmp = header.getObjectType().name().getBytes();
    buf.position(PAGE_OBJECT_TYPE_OFFSET);
    buf.put(bTmp);

    bTmp = header.getStatusCode().name().getBytes();
    buf.position(PAGE_OBJECT_STATUS_OFFSET);
    buf.put(bTmp);

    buf.position(PAGE_USED_DATA_SIZE_OFFSET);
    buf.putInt(header.getUsedDataSize());

    buf.position(PAGE_MODIFIED_TS_OFFSET);
    buf.putLong(header.getModifiedTs());

    buf.position(PAGE_NEXT_PAGE_POS_OFFSET);
    buf.putLong(header.getNextPagePosition());

    buf.position(PAGE_PARENT_PAGE_POS_OFFSET);
    buf.putLong(header.getParentPagePosition());

    buf = null;
    bTmp = null;

    return headerBytes;
  }

  protected JefsPageHeader readPageHeader(long position) throws IOException {
    synchronized (fsLock) {
      JefsPageHeader pageHeader = null;
      byte[] data, bTmp;
      ByteBuffer buf;
      long firstObjectPos, jefsObjectId, jefsParentObjectId, createdTs, objectPageIndex;
      long modifiedTs, nextPagePosition, parentPagePosition;
      int usedDataSize;
      String objectName, sTmp;
      JefsObjectType objectType;
      JefsStatusCodes statusCode;

      firstObjectPos = getFirstRawPagePosition();

      if (position >= firstObjectPos) {
        data = readData(position, fsInfo.getPageHeaderSize());

        buf = ByteBuffer.wrap(data);

        buf.position(PAGE_OBJECT_ID_OFFSET);
        jefsObjectId = buf.getLong();

        buf.position(PAGE_PARENT_OBJECT_ID_OFFSET);
        jefsParentObjectId = buf.getLong();

        buf.position(PAGE_CREATED_TS_OFFSET);
        createdTs = buf.getLong();

        buf.position(PAGE_OBJECT_PAGE_INDEX_OFFSET);
        objectPageIndex = buf.getLong();

        bTmp = new byte[PAGE_OBJECT_NAME_LEN];
        buf.position(PAGE_OBJECT_NANE_OFFSET);
        buf.get(bTmp);
        bTmp = rTrimNull(bTmp);
        if (bTmp != null) {
          objectName = new String(bTmp);
        }
        else {
          objectName = null;
        }

        bTmp = new byte[PAGE_OBJECT_TYPE_LEN];
        buf.position(PAGE_OBJECT_TYPE_OFFSET);
        buf.get(bTmp);
        bTmp = rTrimNull(bTmp);
        sTmp = new String(bTmp);
        sTmp = sTmp.trim();
        objectType = JefsObjectType.valueOf(sTmp);

        bTmp = new byte[PAGE_OBJECT_STATUS_LEN];
        buf.position(PAGE_OBJECT_STATUS_OFFSET);
        buf.get(bTmp);
        bTmp = rTrimNull(bTmp);
        sTmp = new String(bTmp);
        sTmp = sTmp.trim();
        statusCode = JefsStatusCodes.valueOf(sTmp);

        buf.position(PAGE_USED_DATA_SIZE_OFFSET);
        usedDataSize = buf.getInt();

        buf.position(PAGE_MODIFIED_TS_OFFSET);
        modifiedTs = buf.getLong();

        buf.position(PAGE_NEXT_PAGE_POS_OFFSET);
        nextPagePosition = buf.getLong();

        buf.position(PAGE_PARENT_PAGE_POS_OFFSET);
        parentPagePosition = buf.getLong();

        pageHeader = new JefsPageHeader(jefsObjectId, position, createdTs, jefsParentObjectId, objectName, objectType, statusCode, modifiedTs, fsInfo.getPageHeaderSize(), fsInfo.getPageDataSize(),
            objectPageIndex, usedDataSize, nextPagePosition, parentPagePosition);

        sTmp = null;
        bTmp = null;
        buf = null;
        data = null;
      }

      return pageHeader;
    }
  }

  protected byte[] readPageData(JefsPageHeader pageHeader) throws IOException {
    synchronized (fsLock) {
      byte[] data;

      data = readData(pageHeader.getPosition() + pageHeader.getPageHeaderSize(), pageHeader.getPageDataSize());

      return data;
    }
  }

  protected void writePage(JefsPageHeader pageHeader, byte[] pageData) throws IOException {
    synchronized (fsLock) {
      byte[] pageBytes, headerBytes;
      JefsPageHeader parentHeader;

      pageBytes = new byte[fsInfo.getTotalPageSize()];

      // fillNulls(pageBytes);

      headerBytes = getPageHeaderBytes(pageHeader);

      System.arraycopy(headerBytes, 0, pageBytes, 0, headerBytes.length);

      if (pageData != null) {
        System.arraycopy(pageData, 0, pageBytes, fsInfo.getPageHeaderSize(), pageData.length);
      }

      // Read Parent Header if Exists
      if (pageHeader.getParentPagePosition() > 0) {
        parentHeader = readPageHeader(pageHeader.getParentPagePosition());
      }
      else {
        parentHeader = null;
      }

      // Set Object Page Index if NOT Already Set
      /*
       * if (parentHeader != null && pageHeader.getObjectPageIndex() < 0) {
       * pageHeader.setObjectPageIndex(parentHeader.getObjectPageIndex() + 1); }
       */

      // Write Page to Physical File
      writeData(pageBytes, pageHeader.getPosition(), true);

      // Update Parent Page
      if (parentHeader != null && parentHeader.getObjectType() != JefsObjectType.DIRECTORY && parentHeader.getNextPagePosition() != pageHeader.getPosition()) {
        parentHeader.setNextPagePosition(pageHeader.getPosition());

        headerBytes = getPageHeaderBytes(parentHeader);

        writeData(headerBytes, parentHeader.getPosition(), true);
      }

      headerBytes = null;
      pageBytes = null;
      parentHeader = null;
    }
  }

  protected void printPageChainInfo(JefsPageHeader header) throws IOException {
    synchronized (fsLock) {
      long position;

      position = header.getNextPagePosition();

      System.out.println("Page Chain Root at: " + position);
      System.out.println(header);

      while (position > 0) {
        header = readPageHeader(position);

        System.out.println(header);

        position = header.getNextPagePosition();
      }
    }
  }

  protected void deletePage(JefsPageHeader header, byte[] pageData) throws IOException {
    synchronized (fsLock) {
      long ts;

      if (header == null) {
        throw new IOException("Cannot Perform Delete Operation on NULL Header!");
      }

      /*
       * if (header.getNextPagePosition() != 0) { throw new
       * IOException("Cannot Perform Delete Operation Non-Leaf Page!"); }
       */

      ts = System.currentTimeMillis();

      header.setObjectType(JefsObjectType.FREESPACE);
      header.setStatusCode(JefsStatusCodes.DELETED);
      // header.setUsedDataSize(0);
      header.setNextPagePosition(0);
      header.setParentObjectId(0);
      header.setParentPagePosition(0);
      header.setObjectName(null);
      header.setModifiedTs(ts);

      addPageToFreeChain(header);

      writePage(header, pageData);
    }
  }

  protected void truncatePageData(JefsPageHeader header) throws IOException {
    synchronized (fsLock) {
      long ts;

      if (header == null) {
        throw new IOException("Cannot Perform Truncate Operation on NULL Header!");
      }

      ts = System.currentTimeMillis();

      header.setUsedDataSize(0);
      header.setNextPagePosition(0);
      header.setModifiedTs(ts);

      writePage(header, null);
    }
  }

  private void addPageToFreeChain(JefsPageHeader header) throws IOException {
    synchronized (fsLock) {
      long oldBeginPos, oldEndPos, newBeginPos, newEndPos;
      JefsPageHeader oldEndHeader = null;
      byte[] headerBytes;

      oldBeginPos = fsInfo.getFreeChainBeginPosition();
      oldEndPos = fsInfo.getFreeChainEndPosition();

      if (oldBeginPos == fsRaf.length()) {
        newBeginPos = header.getPosition();
        newEndPos = header.getPosition();
      }
      else {
        newBeginPos = oldBeginPos;
        newEndPos = header.getPosition();

        oldEndHeader = readPageHeader(oldEndPos);
        oldEndHeader.setNextPagePosition(newEndPos);

        header.setParentObjectId(oldEndHeader.getObjectId());
        header.setParentPagePosition(oldEndHeader.getPosition());
      }

      fsInfo.setFreeChainBeginPosition(newBeginPos);
      fsInfo.setFreeChainEndPosition(newEndPos);

      if (oldEndHeader != null) {
        headerBytes = getPageHeaderBytes(oldEndHeader);
        writeData(headerBytes, oldEndHeader.getPosition(), true);
        headerBytes = null;
      }

      writeFileSystemInfo();
    }
  }

  protected long sumPageChainDataSize(JefsPageHeader header) throws IOException {
    synchronized (fsLock) {
      long nextPagePos, pageDataSize, pageChainDataSize = 0;
      JefsPageHeader nextHeader;

      nextHeader = header;
      nextPagePos = nextHeader.getNextPagePosition();

      pageDataSize = nextHeader.getUsedDataSize();
      pageChainDataSize += pageDataSize;

      while (nextPagePos != 0) {
        nextHeader = readPageHeader(nextPagePos);

        pageDataSize = nextHeader.getUsedDataSize();
        pageChainDataSize += pageDataSize;

        nextPagePos = nextHeader.getNextPagePosition();
      }

      return pageChainDataSize;
    }
  }

  // END Read/Write Base Volume Data Page Operations------>

  // START Object Search Operations------>

  public long findObjectFirstPagePosition(long objectId) throws IOException {
    synchronized (fsLock) {
      long objectFirstPagePosition = 0, nextPagePos;
      JefsPageHeader header;

      nextPagePos = getFirstRawPagePosition();

      while (nextPagePos < fsRaf.length()) {
        header = readPageHeader(nextPagePos);

        if (header.getObjectId() == objectId && header.getObjectPageIndex() == 0) {
          objectFirstPagePosition = header.getPosition();
          break;
        }

        nextPagePos += fsInfo.getTotalPageSize();
      }

      return objectFirstPagePosition;
    }
  }

  public long findObjectLastPagePosition(long objectId) throws IOException {
    synchronized (fsLock) {
      long nextPagePos, objectLastPagePosition = 0;
      JefsPageHeader header;

      nextPagePos = findObjectFirstPagePosition(objectId);

      while (nextPagePos != 0) {
        header = readPageHeader(nextPagePos);

        if (header.getNextPagePosition() == 0) {
          objectLastPagePosition = header.getPosition();
          break;
        }

        nextPagePos = header.getNextPagePosition();
      }

      return objectLastPagePosition;
    }
  }

  protected long findFirstActiveDataPagePosition() throws IOException {
    synchronized (fsLock) {
      long objectFirstPagePosition = 0, nextPagePos;
      JefsPageHeader header;

      nextPagePos = getFirstRawPagePosition();

      while (nextPagePos != 0) {
        header = readPageHeader(nextPagePos);

        if (header.getStatusCode() == JefsStatusCodes.ACTIVE) {
          objectFirstPagePosition = header.getPosition();
          break;
        }

        nextPagePos = nextPagePos + fsInfo.getTotalPageSize();
      }

      return objectFirstPagePosition;
    }
  }

  protected long getFirstRawPagePosition() throws IOException {
    synchronized (fsLock) {
      long objectFirstPagePosition;

      objectFirstPagePosition = FIRST_OBJECT_POS;

      return objectFirstPagePosition;
    }
  }

  private JefsPageHeader getLastPageHeader(JefsPageHeader header) throws IOException {
    synchronized (fsLock) {
      JefsPageHeader tailHeader;
      long nextPagePos;

      tailHeader = header;
      nextPagePos = tailHeader.getNextPagePosition();

      while (nextPagePos != 0) {
        tailHeader = readPageHeader(nextPagePos);
        nextPagePos = tailHeader.getNextPagePosition();
      }

      return tailHeader;
    }
  }

  // END Object Search Operations-------->

  // START Low Level I/O Operations------>

  protected void writeData(byte[] data, long position, boolean updateModTs) throws IOException {
    synchronized (fsLock) {
      fsRaf.seek(position);
      fsRaf.write(data);

      if (updateModTs) {
        updateFileSystemModTs();
      }
    }
  }

  protected byte[] readData(long position, int length) throws IOException {
    synchronized (fsLock) {
      byte[] data = new byte[length];

      fsRaf.seek(position);
      fsRaf.read(data);

      return data;
    }
  }

  // END Low Level I/O Operations-------->

  // START Utility Functions------>

  protected void fillNulls(byte[] bArr) {
    if (bArr != null) {
      for (int i = 0; i < bArr.length; i++) {
        bArr[i] = (byte) 0;
      }
    }
  }

  protected byte[] rTrimNull(byte[] data) {
    byte[] bArr = null;

    for (int i = data.length - 1; i >= 0; i--) {
      if (data[i] != (byte) 0) {
        bArr = new byte[i + 1];
        System.arraycopy(data, 0, bArr, 0, bArr.length);
        break;
      }
    }

    return bArr;
  }

  protected byte[] cutToLen(byte[] bArr, int maxLen) {
    byte[] newBArr = new byte[maxLen];

    System.arraycopy(bArr, 0, newBArr, 0, newBArr.length);

    return newBArr;
  }

  public byte[] generateRandomData(int maxLen, boolean useTotalLen) {
    byte[] bArr;
    int len, ascii;
    byte b;

    if (useTotalLen) {
      len = maxLen;
    }
    else {
      len = ((int) (Math.random() * maxLen)) + 1;
    }

    bArr = new byte[len];

    for (int i = 0; i < len; i++) {
      ascii = ((int) (Math.random() * 256));
      b = (byte) (ascii <= 127 ? ascii : (ascii - 256));
      bArr[i] = b;
    }

    return bArr;
  }

  public byte[] generateRandomText(int maxLen, boolean useTotalLen) {
    byte[] bArr;
    int len, ascii;
    byte b;

    if (useTotalLen) {
      len = maxLen;
    }
    else {
      len = ((int) (Math.random() * maxLen)) + 1;
    }

    bArr = new byte[len];

    for (int i = 0; i < len; i++) {
      ascii = ((int) (Math.random() * 3));

      switch (ascii) {
        case 0:
          ascii = ((int) (Math.random() * 26));
          ascii = ascii + 65;
          break;
        case 1:
          ascii = ((int) (Math.random() * 26));
          ascii = ascii + 97;
          break;
        case 2:
          ascii = ((int) (Math.random() * 10));
          ascii = ascii + 48;
          break;
      }

      b = (byte) (ascii <= 127 ? ascii : (ascii - 256));
      bArr[i] = b;
    }

    return bArr;
  }

  protected String[] removeEmpties(String[] arr) {
    String[] neArr = null;
    ArrayList<String> al;

    if (arr != null) {
      al = new ArrayList<String>();

      for (int i = 0; i < arr.length; i++) {
        if (arr[i] != null && arr[i].trim().length() > 0) {
          al.add(arr[i]);
        }
      }

      neArr = new String[al.size()];
      neArr = (String[]) al.toArray(neArr);
      al.clear();
      al = null;
    }

    return neArr;
  }

  protected String[] parsePathElements(String path) {
    String[] pathElements = null, tmpArr;

    path = normalizePath(path);

    tmpArr = path.split(JEFS_PATH_SEPARATOR_REGEX);

    if (tmpArr != null) {
      pathElements = new String[tmpArr.length + 1];
      pathElements[0] = JEFS_ROOT_DIRECTORY;

      for (int i = 0; i < tmpArr.length; i++) {
        pathElements[i + 1] = tmpArr[i].trim();
      }

      pathElements = removeEmpties(pathElements);
    }

    return pathElements;
  }

  protected String normalizePath(String path) {
    String nPath = null;

    if (path != null) {
      nPath = path.trim();

      nPath = nPath.replaceAll(DOS_PATH_SEPARATOR_REGEX, JEFS_PATH_SEPARATOR);
    }

    return nPath;
  }

  protected String[] removeLastElement(String[] pathElements) {
    String[] trimmedPathElements;

    if (pathElements == null || pathElements.length <= 1) {
      return null;
    }

    trimmedPathElements = new String[pathElements.length - 1];

    System.arraycopy(pathElements, 0, trimmedPathElements, 0, trimmedPathElements.length);

    return trimmedPathElements;
  }

  public String[] splitDirAndFile(String path) {
    String[] arr = new String[2];
    int lastIndex;

    if (path != null && path.trim().length() > 0) {
      path = path.trim();

      if (!path.endsWith("/")) {
        lastIndex = path.lastIndexOf("/");

        if (lastIndex >= 0) {
          arr[0] = path.substring(0, lastIndex);
          arr[1] = path.substring(lastIndex + 1, path.length());
        }
        else {
          arr[0] = "/"; // Assume Root Directory
          arr[1] = path;
        }
      }
      else if (path.length() > 1) {
        arr[0] = path.substring(0, path.length() - 1);
      }
      else {
        arr[0] = path;
      }
    }

    return arr;
  }

  // END Utility Functions-------->

}
