/**
 * Created Feb 25, 2021
 */
package com.ilardi.systems.jefs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author robert.ilardi
 *
 */

public class JefsFile {

  private final JefsFileSystem fileSystem;

  private final JefsFileInfo fileInfo;

  private final Object fLock;

  private final long firstPagePosition;

  public JefsFile(JefsFileSystem fileSystem, JefsFileInfo fileInfo) {
    this.fileSystem = fileSystem;

    this.fileInfo = fileInfo;

    fLock = new Object();

    firstPagePosition = fileInfo.getPosition();
  }

  @Override
  public String toString() {
    return "JefsFile [fileInfo=" + fileInfo + ", fileSystem=" + fileSystem + "]";
  }

  public JefsFileSystem getFileSystem() {
    return fileSystem;
  }

  public JefsFileInfo getFileInfo() {
    return fileInfo;
  }

  public long getFirstPagePosition() {
    return firstPagePosition;
  }

  public int getFileSystemInfoPageDataSize() {
    synchronized (fLock) {
      int pageDataSize;

      pageDataSize = fileSystem.getFileSystemInfoPageDataSize();

      return pageDataSize;
    }
  }

  public JefsPage readFirstPage() throws IOException {
    synchronized (fLock) {
      JefsPage page;

      page = fileSystem.readPageByPosition(firstPagePosition);

      return page;
    }
  }

  public JefsPage readLastPage() throws IOException {
    synchronized (fLock) {
      JefsPage page;
      long lastPagePos;

      lastPagePos = fileSystem.findObjectLastPagePosition(fileInfo.getObjectId());
      page = fileSystem.readPageByPosition(lastPagePos);

      return page;
    }
  }

  public JefsPage readPage(long position) throws IOException {
    synchronized (fLock) {
      JefsPage page;

      page = fileSystem.readPageByPosition(position);

      return page;
    }
  }

  public void delete() throws IOException {
    synchronized (fLock) {
      fileSystem.rmfile(fileInfo.getFullPath());
    }
  }

  public void rename(String newName) throws IOException {
    synchronized (fLock) {
      fileSystem.rename(fileInfo.getPosition(), newName);
      renameFileInfo(newName);
    }
  }

  private void renameFileInfo(String newName) {
    synchronized (fLock) {
      String fullPath, tmp;
      StringBuilder sb;
      int idx;

      fileInfo.setObjectName(newName);

      fullPath = fileInfo.getFullPath();

      if (fullPath.indexOf("/") >= 0) {
        idx = fullPath.lastIndexOf("/");
        tmp = fullPath.substring(0, idx + 1);

        sb = new StringBuilder();
        sb.append(tmp);
        sb.append(newName);

        tmp = sb.toString();

        fullPath = tmp;
      }
      else {
        fullPath = newName;
      }

      fileInfo.setFullPath(fullPath);
    }
  }

  public long size() throws IOException {
    synchronized (fLock) {
      long sz;

      sz = fileSystem.determineFileSize(fileInfo.getFullPath());

      return sz;
    }
  }

  public JefsPage appendPageData(JefsPage tailPage, byte[] newPageData, int newPageDataLen) throws IOException {
    synchronized (fLock) {
      JefsPage nextPage;
      byte[] tmpData;

      if (newPageDataLen != newPageData.length) {
        tmpData = new byte[newPageDataLen];
        System.arraycopy(newPageData, 0, tmpData, 0, newPageDataLen);
      }
      else {
        tmpData = newPageData;
      }

      nextPage = fileSystem.appendDataToPageChain(tailPage, tmpData);
      tmpData = null;

      return nextPage;
    }
  }

  public void truncatePageChain() throws IOException {
    synchronized (fLock) {
      JefsPage page = readFirstPage();
      fileSystem.truncatePageChainDataToPage(page);
    }
  }

  public void updateFileInfo(JefsPageHeader header) {
    synchronized (fLock) {
      fileInfo.update(header);
    }
  }

  public InputStream openInputStream() throws IOException {
    synchronized (fLock) {
      JefsInputStream ins;

      ins = new JefsInputStream(this);

      return ins;
    }
  }

  public OutputStream openOutputStream(boolean appendMode) throws IOException {
    synchronized (fLock) {
      JefsOutputStream outs;

      outs = new JefsOutputStream(this, appendMode);

      return outs;
    }
  }

  public JefsRandomAccessFile openRandomAccessFile() throws IOException {
    synchronized (fLock) {
      JefsRandomAccessFile raf;

      raf = new JefsRandomAccessFile(this);

      return raf;
    }
  }

  public JefsPage appendEmptyPage(JefsPage prevPage) throws IOException {
    synchronized (fLock) {
      JefsPage newPage;

      newPage = fileSystem.allocatePage(prevPage.getObjectId(), prevPage.getPosition(), prevPage.getObjectName(), prevPage.getObjectType(), prevPage.getObjectPageIndex() + 1, null);

      return newPage;
    }
  }

  public void writePage(JefsPage curPage) throws IOException {
    synchronized (fLock) {
      fileSystem.writePage(curPage);
    }
  }

}
