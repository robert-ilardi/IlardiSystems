/**
 * Created Feb 25, 2021
 */
package com.ilardi.systems.jefs;

import static com.ilardi.systems.jefs.JefsConstants.FAT_PAGE_ENTRY_LEN;
import static com.ilardi.systems.jefs.JefsConstants.JEFS_FAT_PAGE_NAME;
import static com.ilardi.systems.jefs.JefsConstants.JEFS_MAGIC_BYTES;
import static com.ilardi.systems.jefs.JefsConstants.JEFS_ROOT_DIRECTORY;
import static com.ilardi.systems.jefs.JefsConstants.JEFS_VERSION;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author robert.ilardi
 *
 */

public class JefsFileSystem extends JefsPageFileSystem {

  public JefsFileSystem(String physicalFilePath) throws IOException {
    this(physicalFilePath, null);
  }

  public JefsFileSystem(String physicalFilePath, String volumeName) throws IOException {
    this(JEFS_MAGIC_BYTES, JEFS_VERSION, physicalFilePath, volumeName);
  }

  public JefsFileSystem(String magicBytes, int version, String physicalFilePath, String volumeName) throws IOException {
    super(magicBytes, version, physicalFilePath, volumeName);
  }

  // START File System Init Functions------>

  protected void initNewFileSystem() throws IOException {
    synchronized (fsLock) {
      super.initNewFileSystem();
      createRootFileAllocationTable();
      createRootDir();
    }
  }

  private void createRootFileAllocationTable() throws IOException {
    synchronized (fsLock) {
      allocatePage(0, 0, JEFS_FAT_PAGE_NAME, JefsObjectType.FILE_ALLOCATION_TABLE, 0, null);
    }
  }

  private void createRootDir() throws IOException {
    synchronized (fsLock) {
      allocatePage(0, 0, JEFS_ROOT_DIRECTORY, JefsObjectType.DIRECTORY, 0, null);
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
   * synchronized (fsLock) { ArrayList<Long> rootPagePositions; JefsPage fatPage;
   * long position;
   * 
   * position = getFirstJefsFatPagePosition();
   * 
   * rootPagePositions = new ArrayList<Long>();
   * 
   * while (position > 0) { fatPage = readPageByPosition(position);
   * 
   * readAllRootPagePositions(fatPage, rootPagePositions);
   * 
   * position = fatPage.getNextPagePosition(); }
   * 
   * return rootPagePositions; } }
   */

  @Override
  public long getStartingRootPagePosition() throws IOException {
    return getFirstJefsFatPagePosition();
  }

  @Override
  public List<Long> getNextRootPagePositions(long startingRootPagePos) throws IOException {
    synchronized (fsLock) {
      ArrayList<Long> rootPagePositions;
      JefsPage fatPage;
      long position;

      position = startingRootPagePos;

      rootPagePositions = new ArrayList<Long>();

      if (position > 0) {
        fatPage = readPageByPosition(position);

        readAllRootPagePositions(fatPage, rootPagePositions);

        position = fatPage.getNextPagePosition();

        rootPagePositions.add(0, position); // Add Next Root Page Position
      }

      return rootPagePositions;
    }
  }

  public void mkdir(String dir) throws IOException {
    synchronized (fsLock) {
      String[] path;
      JefsDirEntry dirEntry;
      JefsPageHeader rootHeader;

      dir = normalizePath(dir);

      rootHeader = readRootDirHeader();

      path = parsePathElements(dir);

      dirEntry = new JefsDirEntry();
      dirEntry.setHeader(rootHeader);

      mkdir(dirEntry, path, 0);
    }
  }

  public void printDirectoryTree(boolean printHeaderInfo) throws IOException {
    synchronized (fsLock) {
      JefsDirEntry dir;
      JefsPageHeader rootHeader;

      rootHeader = readRootDirHeader();

      dir = new JefsDirEntry();
      dir.setHeader(rootHeader);

      printDirectoryTree(dir, 0, printHeaderInfo);
    }
  }

  public void rmdir(String dir) throws IOException {
    synchronized (fsLock) {
      JefsDirEntry dirEntry;

      dir = normalizePath(dir);

      dirEntry = findTailDirectoryEntry(dir);

      rmdir(dirEntry);
    }
  }

  public ArrayList<JefsDirEntry> list(String path) throws IOException {
    synchronized (fsLock) {
      JefsDirEntry dirEntry;
      ArrayList<JefsDirEntry> dirEntries;

      path = normalizePath(path);

      dirEntry = findTailDirectoryEntry(path);

      dirEntries = list(dirEntry);

      return dirEntries;
    }
  }

  public long determineFileSize(String path) throws IOException {
    synchronized (fsLock) {
      long fileSize;
      JefsDirEntry dirEntry;

      path = normalizePath(path);

      dirEntry = findTailDirectoryEntry(path);

      fileSize = determineFileSize(dirEntry);

      return fileSize;
    }
  }

  public void rmfile(String path) throws IOException {
    synchronized (fsLock) {
      JefsDirEntry dirEntry;

      path = normalizePath(path);

      dirEntry = findTailDirectoryEntry(path);

      rmfile(dirEntry);
    }
  }

  public JefsFile openFile(String path) throws IOException {
    synchronized (fsLock) {
      JefsFile file;
      JefsDirEntry dirEntry, fileEntry;
      String[] filePath;
      String dirName, filename;

      path = normalizePath(path);

      filePath = splitDirAndFile(path);
      dirName = filePath[0];
      filename = filePath[1];

      dirEntry = findTailDirectoryEntry(dirName);

      if (dirEntry == null) {
        throw new IOException("Directory Not Found!");
      }

      if (dirEntry.getHeader().getObjectType() != JefsObjectType.DIRECTORY) {
        throw new IOException("Directory Entry is Not a Directory Object!");
      }

      if (containsFile(dirEntry, filename)) {
        fileEntry = findTailDirectoryEntry(path);
        file = openFile(fileEntry, path);
      }
      else {
        file = createFile(dirEntry, filename, path);
      }

      return file;
    }
  }

  // END Public High Level Operations-------->

  // START Page Level I/O Operations------>

  private JefsFile createFile(JefsDirEntry parentDir, String filename, String fullPath) throws IOException {
    synchronized (fsLock) {
      JefsFile file;
      JefsPageHeader fileHeader, parentHeader;
      JefsPage filePage;
      JefsFileInfo fileInfo;

      filename = filename.trim();

      if (".".equals(filename)) {
        throw new IOException("File Cannot be Named '.'");
      }

      if ("..".equals(filename)) {
        throw new IOException("File Cannot be Named '..'");
      }

      if ("/".equals(filename)) {
        throw new IOException("File Cannot be Named '/'");
      }

      parentHeader = parentDir.getHeader();

      filePage = allocatePage(parentHeader.getObjectId(), parentHeader.getPosition(), filename, JefsObjectType.FILE, 0, null);
      fileHeader = filePage.getHeader();

      fileInfo = new JefsFileInfo(fileHeader, fullPath);

      file = new JefsFile(this, fileInfo);

      return file;
    }
  }

  private JefsFile openFile(JefsDirEntry fileEntry, String fullPath) throws IOException {
    synchronized (fsLock) {
      JefsFile file;
      JefsPageHeader header;

      if (fileEntry == null) {
        return null;
      }

      header = fileEntry.getHeader();

      file = openFile(header, fullPath);

      return file;
    }
  }

  private JefsFile openFile(JefsPageHeader header, String fullPath) throws IOException {
    synchronized (fsLock) {
      JefsFile file;
      JefsFileInfo fileInfo;

      if (header == null) {
        return null;
      }

      if (header.getObjectType() != JefsObjectType.FILE) {
        return null;
      }

      fileInfo = new JefsFileInfo(header, fullPath);

      file = new JefsFile(this, fileInfo);

      return file;
    }
  }

  private void mkdir(JefsDirEntry dir, String[] path, int pathIndex) throws IOException {
    synchronized (fsLock) {
      ArrayList<JefsDirEntry> subDirs;
      JefsDirEntry subDir;
      String pathElement;

      if (pathIndex >= path.length) {
        return;
      }

      pathElement = path[pathIndex];

      if (pathElement.equals(dir.getHeader().getObjectName())) {
        if (pathIndex + 1 < path.length) {
          pathElement = path[pathIndex + 1];

          readSubDirectories(dir);
          subDirs = dir.getEntries();

          subDir = matchPathElement(subDirs, pathElement);

          if (subDir != null) {
            mkdir(subDir, path, pathIndex + 1);
          }
          else {
            for (int i = pathIndex + 1; i < path.length; i++) {
              dir = createDir(dir, path[i]);
            }
          }
        }
      }
    }
  }

  private void rmfile(JefsDirEntry dirEntry) throws IOException {
    synchronized (fsLock) {
      JefsPageHeader header;

      if (dirEntry == null) {
        return;
      }

      header = dirEntry.getHeader();

      rmfile(header);

      header = null;
    }
  }

  private void rmfile(JefsPageHeader header) throws IOException {
    synchronized (fsLock) {
      JefsPage page;

      if (header == null) {
        return;
      }

      if (header.getObjectType() != JefsObjectType.FILE) {
        return;
      }

      page = readPageByPosition(header.getPosition());

      deletePageChainUsingPageIndex(page);

      page = null;
    }
  }

  private void printDirectoryTree(JefsDirEntry dir, int level, boolean printHeaderInfo) throws IOException {
    synchronized (fsLock) {
      ArrayList<JefsDirEntry> subDirs;
      JefsDirEntry subDir;
      JefsPageHeader header;
      StringBuilder sb;
      String tmp;

      sb = new StringBuilder();

      for (int i = 0; i < level; i++) {
        sb.append("  ");
      }

      header = dir.getHeader();

      if (printHeaderInfo) {
        sb.append(header);
      }
      else {
        sb.append(header.getObjectName());
      }

      tmp = sb.toString();
      System.out.println(tmp);

      readSubDirectories(dir);
      subDirs = dir.getEntries();

      if (subDirs != null) {
        Collections.sort(subDirs, new DirNameComparator());

        level++;

        for (int i = 0; i < subDirs.size(); i++) {
          subDir = subDirs.get(i);

          printDirectoryTree(subDir, level, printHeaderInfo);
        }
      }
    }
  }

  private ArrayList<JefsDirEntry> list(JefsDirEntry dirEntry) throws IOException {
    synchronized (fsLock) {
      ArrayList<JefsDirEntry> dirEntries;

      if (dirEntry == null) {
        return null;
      }

      readDirectoryListing(dirEntry);
      dirEntries = dirEntry.getEntries();

      return dirEntries;
    }
  }

  private void rmdir(JefsDirEntry dir) throws IOException {
    synchronized (fsLock) {
      JefsPageHeader dirHeader;
      ArrayList<JefsDirEntry> dirEntries;
      JefsPage page;

      if (dir == null) {
        return;
      }

      dirHeader = dir.getHeader();

      if (dirHeader.getObjectType() != JefsObjectType.DIRECTORY) {
        throw new IOException("Object '" + dirHeader.getObjectName() + "' is NOT a Directory");
      }

      readDirectoryListing(dir);
      dirEntries = dir.getEntries();

      if (dirEntries != null && !dirEntries.isEmpty()) {
        throw new IOException("Directory '" + dirHeader.getObjectName() + "' is NOT Empty");
      }

      page = readPageByPosition(dirHeader.getPosition());
      deletePage(page);
    }
  }

  private long determineFileSize(JefsDirEntry dirEntry) throws IOException {
    synchronized (fsLock) {
      long fileSize;
      JefsPageHeader header;

      if (dirEntry == null) {
        return 0;
      }

      header = dirEntry.getHeader();

      fileSize = determineFileSize(header);

      return fileSize;
    }
  }

  private long determineFileSize(JefsPageHeader header) throws IOException {
    synchronized (fsLock) {
      long fileSize;

      if (header == null) {
        return 0;
      }

      if (header.getObjectType() != JefsObjectType.FILE) {
        return 0;
      }

      fileSize = sumPageChainDataSize(header);

      return fileSize;
    }
  }

  private JefsPageHeader readRootDirHeader() throws IOException {
    synchronized (fsLock) {
      long rootPos = getFirstJefsDataPagePosition();

      JefsPageHeader rootHeader = readPageHeader(rootPos);

      return rootHeader;
    }
  }

  private JefsDirEntry createDir(JefsDirEntry parentDir, String dirName) throws IOException {
    synchronized (fsLock) {
      JefsDirEntry dir;
      JefsPageHeader dirHeader;
      JefsPage dirPage;
      ArrayList<JefsDirEntry> entries;

      dirName = dirName.trim();

      if (".".equals(dirName)) {
        throw new IOException("Directory Cannot be Named '.'");
      }

      if ("..".equals(dirName)) {
        throw new IOException("Directory Cannot be Named '..'");
      }

      dirPage = allocatePage(parentDir.getHeader().getObjectId(), parentDir.getHeader().getPosition(), dirName, JefsObjectType.DIRECTORY, 0, null);
      dirHeader = dirPage.getHeader();

      dir = new JefsDirEntry();
      dir.setHeader(dirHeader);
      dir.setParent(parentDir);

      entries = parentDir.getEntries();

      if (entries == null) {
        entries = new ArrayList<JefsDirEntry>();
      }

      entries.add(dir);

      return dir;
    }
  }

  @SuppressWarnings("unused")
  @Override
  protected void readAllRootPagePositions(JefsPage fatPage, ArrayList<Long> rootPagePositions) throws IOException {
    synchronized (fsLock) {
      long fatLoc, objPos, objId, parentObjId, parentPos;
      ByteBuffer buf;

      buf = ByteBuffer.wrap(fatPage.getData());

      objPos = 0;
      fatLoc = 0;

      while (fatLoc < fsInfo.getPageDataSize()) {
        objPos = buf.getLong();
        objId = buf.getLong();
        parentObjId = buf.getLong();
        parentPos = buf.getLong();

        /*
         * if (objPos == 0) { break; } else { rootPagePositions.add(objPos); fatLoc +=
         * FAT_PAGE_ENTRY_LEN; }
         */

        if (objPos != 0) {
          rootPagePositions.add(objPos);
        }

        fatLoc += FAT_PAGE_ENTRY_LEN;
      }

      buf = null;
    }
  }

  protected void writePage(JefsPageHeader pageHeader, byte[] pageData) throws IOException {
    synchronized (fsLock) {
      if ((pageHeader.getObjectType() == JefsObjectType.DIRECTORY || pageHeader.getObjectType() == JefsObjectType.FILE) && pageHeader.getObjectPageIndex() == 0
          && pageHeader.getStatusCode() != JefsStatusCodes.DELETED) {
        insertFileAllocationTableEntry(pageHeader.getPosition(), pageHeader.getObjectId(), pageHeader.getParentObjectId(), pageHeader.getParentPagePosition());
      }

      super.writePage(pageHeader, pageData);
    }
  }

  private void insertFileAllocationTableEntry(long position, long objectId, long parentObjectId, long parentPosition) throws IOException {
    synchronized (fsLock) {
      byte[] bArr;
      ByteBuffer buf;
      long fatPos;

      fatPos = findFatLocationPosition(position);

      if (fatPos > 0) {
        return;
      }

      fatPos = findFreeFatLocationPosition();

      bArr = new byte[FAT_PAGE_ENTRY_LEN];

      fillNulls(bArr);

      buf = ByteBuffer.wrap(bArr);

      buf.putLong(position);
      buf.putLong(objectId);
      buf.putLong(parentObjectId);
      buf.putLong(parentPosition);

      writeData(bArr, fatPos, true);

      buf = null;
      bArr = null;
    }
  }

  private long allocateNewFileAllocationTable(JefsPageHeader lastFatHeader) throws IOException {
    synchronized (fsLock) {
      long freePatLocPos;
      JefsPageHeader fatHeader;
      JefsPage fatPage;

      fatPage = allocatePage(lastFatHeader.getParentObjectId(), lastFatHeader.getParentPagePosition(), JEFS_FAT_PAGE_NAME, JefsObjectType.FILE_ALLOCATION_TABLE, lastFatHeader.getObjectPageIndex() + 1,
          null);

      fatHeader = fatPage.getHeader();

      freePatLocPos = fatHeader.getPosition() + fatHeader.getPageHeaderSize();

      return freePatLocPos;
    }
  }

  protected void deletePage(JefsPageHeader header, byte[] pageData) throws IOException {
    synchronized (fsLock) {
      if (header == null) {
        throw new IOException("Cannot Perform Delete Operation on NULL Header!");
      }

      if ((header.getObjectType() == JefsObjectType.DIRECTORY || header.getObjectType() == JefsObjectType.FILE) && header.getObjectPageIndex() == 0) {
        removeFileAllocationTableEntry(header.getPosition());
      }

      super.deletePage(header, pageData);
    }
  }

  private void removeFileAllocationTableEntry(long rootPagePosition) throws IOException {
    synchronized (fsLock) {
      byte[] bArr;
      long fatPos;

      fatPos = findFatLocationPosition(rootPagePosition);

      if (fatPos > 0) {
        bArr = new byte[FAT_PAGE_ENTRY_LEN];

        fillNulls(bArr);

        writeData(bArr, fatPos, true);

        bArr = null;
      }
    }
  }

  // END Page Level I/O Operations-------->

  // START Object Search Operations------>

  private boolean containsFile(JefsDirEntry dirEntry, String filename) {
    synchronized (fsLock) {
      boolean contains = false;
      ArrayList<JefsDirEntry> entries;
      JefsDirEntry entry;
      JefsPageHeader header;

      entries = dirEntry.getEntries();

      if (entries != null) {
        for (int i = 0; i < entries.size(); i++) {
          entry = entries.get(i);
          header = entry.getHeader();

          if (filename.equals(header.getObjectName())) {
            contains = true;
            break;
          }
        }
      }

      return contains;
    }
  }

  private JefsDirEntry findTailDirectoryEntry(String path) throws IOException {
    synchronized (fsLock) {
      String[] pathElements;
      JefsDirEntry dirEntry;
      JefsPageHeader rootHeader;

      path = normalizePath(path);

      rootHeader = readRootDirHeader();

      pathElements = parsePathElements(path);

      dirEntry = new JefsDirEntry();
      dirEntry.setHeader(rootHeader);

      dirEntry = findTailDirectoryEntry(dirEntry, pathElements, 0);

      return dirEntry;
    }
  }

  private JefsDirEntry findTailDirectoryEntry(JefsDirEntry dir, String[] path, int pathIndex) throws IOException {
    synchronized (fsLock) {
      ArrayList<JefsDirEntry> dirEntries;
      JefsDirEntry dirEntry, tailEntry = null;
      String pathElement;

      if (pathIndex >= path.length) {
        return null;
      }

      pathElement = path[pathIndex];

      if (pathElement.equals(dir.getHeader().getObjectName())) {
        if (pathIndex + 1 < path.length) {
          pathElement = path[pathIndex + 1];

          readDirectoryListing(dir);
          dirEntries = dir.getEntries();

          dirEntry = matchPathElement(dirEntries, pathElement);

          if (dirEntry != null) {
            tailEntry = findTailDirectoryEntry(dirEntry, path, pathIndex + 1);
          }
        }
        else if (pathIndex + 1 == path.length) {
          tailEntry = dir;
        }
      }

      return tailEntry;
    }
  }

  private void readSubDirectories(JefsDirEntry dir) throws IOException {
    synchronized (fsLock) {
      JefsPage fatPage;
      long position;

      position = getFirstJefsFatPagePosition();

      while (position > 0) {
        fatPage = readPageByPosition(position);

        readDirectoryListing(dir, fatPage, true, false);

        position = fatPage.getNextPagePosition();
      }
    }
  }

  private void readDirectoryListing(JefsDirEntry dir) throws IOException {
    synchronized (fsLock) {
      JefsPage fatPage;
      long position;

      position = getFirstJefsFatPagePosition();

      while (position > 0) {
        fatPage = readPageByPosition(position);

        readDirectoryListing(dir, fatPage, true, true);

        position = fatPage.getNextPagePosition();
      }
    }
  }

  @SuppressWarnings("unused")
  private void readDirectoryListing(JefsDirEntry dir, JefsPage fatPage, boolean includeSubDirs, boolean includeFiles) throws IOException {
    synchronized (fsLock) {
      long fatLoc, objPos, objId, parentObjId, parentPos;
      ByteBuffer buf;
      JefsPageHeader dirEntryHeader;
      ArrayList<JefsDirEntry> dirEntries;
      JefsDirEntry dirEntry;

      buf = ByteBuffer.wrap(fatPage.getData());

      objPos = 0;
      fatLoc = 0;

      while (fatLoc < fsInfo.getPageDataSize()) {
        objPos = buf.getLong();
        objId = buf.getLong();
        parentObjId = buf.getLong();
        parentPos = buf.getLong();

        if (parentObjId > 0 && parentObjId == dir.getHeader().getObjectId()) {
          dirEntryHeader = readPageHeader(objPos);

          if (dirEntryHeader.getStatusCode() != JefsStatusCodes.DELETED) {
            if ((includeSubDirs && dirEntryHeader.getObjectType() == JefsObjectType.DIRECTORY) || (includeFiles && dirEntryHeader.getObjectType() == JefsObjectType.FILE)) {
              dirEntry = new JefsDirEntry();

              dirEntry.setHeader(dirEntryHeader);
              dirEntry.setParent(dir);

              dirEntries = dir.getEntries();

              if (dirEntries == null) {
                dirEntries = new ArrayList<JefsDirEntry>();
                dir.setEntries(dirEntries);
              }

              dirEntries.add(dirEntry);
            }
          }
        }

        fatLoc += FAT_PAGE_ENTRY_LEN;
      }

      buf = null;
    }
  }

  protected long getFirstJefsDataPagePosition() throws IOException {
    synchronized (fsLock) {
      long objectFirstPagePosition;

      objectFirstPagePosition = getFirstJefsFatPagePosition();
      objectFirstPagePosition += fsInfo.getTotalPageSize();

      return objectFirstPagePosition;
    }
  }

  protected long getFirstJefsFatPagePosition() throws IOException {
    synchronized (fsLock) {
      long objectFirstPagePosition;

      objectFirstPagePosition = getFirstPageFsDataPagePosition();

      return objectFirstPagePosition;
    }
  }

  private long findFreeFatLocationPosition() throws IOException {
    synchronized (fsLock) {
      long freeFatLocPos = 0, nextPagePos;
      JefsPageHeader header = null;

      nextPagePos = getFirstJefsFatPagePosition();

      while (nextPagePos < fsRaf.length()) {
        header = readPageHeader(nextPagePos);

        if (header.getObjectType() == JefsObjectType.FILE_ALLOCATION_TABLE) {
          freeFatLocPos = findFreeFatLocationPosition(header);

          if (freeFatLocPos >= 0) {
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

      if (freeFatLocPos == 0) {
        freeFatLocPos = allocateNewFileAllocationTable(header);
      }

      return freeFatLocPos;
    }
  }

  @SuppressWarnings("unused")
  private long findFreeFatLocationPosition(JefsPageHeader fatHeader) throws IOException {
    synchronized (fsLock) {
      long freeFatLocPos, fatLoc, objPos, objId, parentObjId, parentPos;
      JefsPage rootPage;
      ByteBuffer buf;

      rootPage = readPageByPosition(fatHeader.getPosition());

      buf = ByteBuffer.wrap(rootPage.getData());

      objPos = 0;
      fatLoc = 0;

      while (fatLoc < fsInfo.getPageDataSize()) {
        objPos = buf.getLong();
        objId = buf.getLong();
        parentObjId = buf.getLong();
        parentPos = buf.getLong();

        if (objPos == 0) {
          break;
        }

        fatLoc += FAT_PAGE_ENTRY_LEN;
      }

      if (fatLoc < fsInfo.getPageDataSize() && objPos == 0) {
        freeFatLocPos = fatHeader.getPosition() + fsInfo.getPageHeaderSize() + fatLoc;
      }
      else {
        freeFatLocPos = -1;
      }

      buf = null;
      rootPage = null;

      return freeFatLocPos;
    }
  }

  @SuppressWarnings("unused")
  private long findFatLocationPosition(long fatPagePosition) throws IOException {
    long fatLocPos, nextPagePos, objPos, objId, parentObjId, parentPos, fatPosInFile = 0;
    JefsPage fatPage;
    ByteBuffer buf;

    nextPagePos = getFirstJefsFatPagePosition();

    while (nextPagePos > 0) {
      fatPage = readPageByPosition(nextPagePos);

      buf = ByteBuffer.wrap(fatPage.getData());

      objPos = 0;
      fatLocPos = 0;

      while (fatLocPos < fsInfo.getPageDataSize()) {
        objPos = buf.getLong();
        objId = buf.getLong();
        parentObjId = buf.getLong();
        parentPos = buf.getLong();

        if (objPos == fatPagePosition) {
          fatPosInFile = fatPage.getPosition() + fsInfo.getPageHeaderSize() + fatLocPos;
          break;
        }

        fatLocPos += FAT_PAGE_ENTRY_LEN;
      }

      if (fatPosInFile > 0) {
        break;
      }

      nextPagePos = fatPage.getNextPagePosition();
    }

    return fatPosInFile;
  }

  // END Object Search Operations-------->

  // START Utility Functions------>

  private JefsDirEntry matchPathElement(ArrayList<JefsDirEntry> dirEntries, String pathElement) {
    JefsDirEntry dirEntry = null;

    if (dirEntries != null) {
      for (int i = 0; i < dirEntries.size(); i++) {
        dirEntry = dirEntries.get(i);

        if (pathElement.equals(dirEntry.getHeader().getObjectName())) {
          break;
        }
        else {
          dirEntry = null;
        }
      }
    }

    return dirEntry;
  }

  // END Utility Functions-------->

  @Override
  public String toString() {
    return "JefsFileSystem [fsInfo=" + fsInfo + "]";
  }

  public static void main(String[] args) {
    JefsFileSystem jefs = null;
    String jefsVolumeFilePath;
    int exitCd;
    JefsDirEntry dirEntry;
    ArrayList<JefsDirEntry> dirEntries;
    JefsFile jFile;

    if (args.length != 1) {
      exitCd = 1;
      System.err.println("Usage: java " + JefsFileSystem.class.getName() + " [JEFS_VOLUME_FILE_PATH]");
    }
    else {
      try {
        jefsVolumeFilePath = args[0];
        jefsVolumeFilePath = jefsVolumeFilePath.trim();

        jefs = new JefsFileSystem(jefsVolumeFilePath);

        jefs.format();

        jefs.open();

        jefs.printFileSystemInfo();

        jefs.updateVolumeName("Robert's JEFS Volume");

        jefs.printFileSystemInfo();

        jefs.mkdir("/home/rilardi");

        jefs.mkdir("/home/panglo");

        jefs.mkdir("/home/rilardi/documents/");
        jefs.mkdir("/home/rilardi/downloads/");
        jefs.mkdir("/home/rilardi/pictures/");
        jefs.mkdir("/home/rilardi/videos/");

        jefs.mkdir("/home/rilardi/pictures/trips");
        jefs.mkdir("/home/rilardi/pictures/pets");

        jefs.mkdir("/home/rilardi/pictures/pets/lily/photos1/photos2");

        jefs.mkdir("/home/rilardi/pictures/pets/thor");

        jefs.mkdir("/home/rilardi/pictures/pets/lily/photos1/photos2/photos3/photos4/photos5");

        jefs.mkdir("/etc");

        jefs.mkdir("/bin");

        jefs.mkdir("/");

        jefs.mkdir("/usr/lib");

        jefs.mkdir("/usr/include");

        jefs.mkdir("/robert/data/");
        jefs.mkdir("/data/");

        jefs.mkdir("/tmp/");

        jefs.mkdir("\\robert\\data\\test\\");

        jefs.mkdir("/tmp/Robert Ilardi Temp");

        jefs.mkdir("/tmp/Robert Ilardi Temp/test1");

        try {
          jefs.mkdir("/tmp/test1/This is a Junk Temp Directory that needs to have a very long file name so we can test the maximum sub directory or file name length imposed by JEFS");
        }
        catch (Exception e) {
          e.printStackTrace();
        }

        try {
          jefs.mkdir("/tmp/.");
        }
        catch (Exception e) {
          e.printStackTrace();
        }

        try {
          jefs.mkdir("/tmp/..");
        }
        catch (Exception e) {
          e.printStackTrace();
        }

        jefs.mkdir("/tmp/test2.data");

        jefs.printDirectoryTree(true);

        jefs.mkdir("/tmp/Robert Ilardi Temp/junk");
        jefs.mkdir("/tmp/Robert Ilardi Temp/junk");

        jefs.printDirectoryTree(false);

        try {
          jefs.rmdir("/tmp/Robert Ilardi Temp/");
        }
        catch (Exception e) {
          e.printStackTrace();
        }

        jefs.rmdir("/tmp/Robert Ilardi Temp/junk");
        jefs.rmdir("/tmp/Robert Ilardi Temp/junk");

        jefs.rmdir("/tmp/Robert Ilardi Temp/test1");

        jefs.printDirectoryTree(false);

        dirEntry = jefs.findTailDirectoryEntry("/home/rilardi/pictures/pets/lily/photos1/photos2/photos3/photos4/photos5");
        System.out.println(dirEntry);

        dirEntry = jefs.findTailDirectoryEntry("/home/rilardi/pictures/pets/lily/photos1/photos2/photos3/photos4/photos5/dog.jpg");
        System.out.println(dirEntry);

        dirEntry = jefs.findTailDirectoryEntry("/");
        System.out.println(dirEntry);

        dirEntry = jefs.findTailDirectoryEntry("/home/rilardi/pictures");
        System.out.println(dirEntry);

        jefs.printFileSystemInfo();

        jefs.printPageChainInfo();

        jefs.printFreeChainInfo();

        jFile = jefs.openFile("/home/rilardi/temp.txt");

        System.out.println(jFile);

        dirEntries = jefs.list("/home/rilardi");

        System.out.println("Directory Listing for: /home/rilardi");

        for (int i = 0; i < dirEntries.size(); i++) {
          dirEntry = dirEntries.get(i);
          System.out.println(dirEntry);
        }

        // jefs.rename(jFile.getFileInfo().getPosition(), "temp2.txt");
        jFile.rename("temp2.txt");

        dirEntries = jefs.list("/home/rilardi");

        System.out.println("Directory Listing for: /home/rilardi");

        for (int i = 0; i < dirEntries.size(); i++) {
          dirEntry = dirEntries.get(i);
          System.out.println(dirEntry);
        }

        exitCd = 0;
      } // End try block
      catch (Exception e) {
        exitCd = 1;
        e.printStackTrace();
      }
      finally {
        try {
          if (jefs != null) {
            jefs.close();
            jefs = null;
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
