/**
 * Created Feb 25, 2021
 */
package com.ilardi.systems.jefs;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author robert.ilardi
 *
 */

public class JefsOutputStream extends OutputStream {

  protected final JefsFile jefsFile;

  protected final Object sLock;

  protected JefsPage curPage;

  protected final byte[] newPageData;

  protected final int pageDataSize;

  protected int newPageDataIdx;

  protected final boolean appendMode;

  protected boolean closed;

  public JefsOutputStream(JefsFile jefsFile, boolean appendMode) throws IOException {
    this.jefsFile = jefsFile;
    this.appendMode = appendMode;

    closed = false;

    sLock = new Object();

    pageDataSize = jefsFile.getFileSystemInfoPageDataSize();
    newPageData = new byte[pageDataSize];

    if (appendMode) {
      curPage = jefsFile.readLastPage();
    }
    else {
      jefsFile.truncatePageChain();
      curPage = jefsFile.readFirstPage();

      JefsPageHeader header = curPage.getHeader();
      jefsFile.updateFileInfo(header);
    }

    newPageDataIdx = 0;
    // newPageDataIdx = curPage.getUsedDataSize();
  }

  @Override
  public void write(int b) throws IOException {
    synchronized (sLock) {
      if (closed) {
        throw new IOException("JEFS Output Stream is Closed!");
      }

      newPageData[newPageDataIdx] = ((byte) (b <= 127 ? b : (b - 256)));
      newPageDataIdx++;

      if (newPageDataIdx >= pageDataSize) {
        curPage = jefsFile.appendPageData(curPage, newPageData, newPageDataIdx);
        newPageDataIdx = 0;
      }
    }
  }

  @Override
  public void flush() throws IOException {
    synchronized (sLock) {
      if (!closed) {
        if (newPageDataIdx > 0) {
          curPage = jefsFile.appendPageData(curPage, newPageData, newPageDataIdx);
          newPageDataIdx = 0;
        }

        super.flush();
      }
    }
  }

  @Override
  public void close() throws IOException {
    synchronized (sLock) {
      if (!closed) {
        flush();
        super.close();
        closed = true;
      }
    }
  }

  @Override
  public String toString() {
    return "JefsOutputStream [jefsFile=" + jefsFile + ", curPage=" + curPage + ", pageDataSize=" + pageDataSize + ", newPageDataIdx=" + newPageDataIdx + ", appendMode=" + appendMode + ", closed="
        + closed + "]";
  }

}
