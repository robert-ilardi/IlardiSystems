/**
 * Created Feb 25, 2021
 */
package com.ilardi.systems.jefs;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author robert.ilardi
 *
 */

public class JefsInputStream extends InputStream {

  private final JefsFile jefsFile;

  private final Object sLock;

  protected long nextPagePos;

  protected JefsPage curPage;
  protected byte[] curPageData;
  protected int curPageDataIdx;

  protected boolean closed;

  public JefsInputStream(JefsFile jefsFile) throws IOException {
    this.jefsFile = jefsFile;

    closed = false;

    sLock = new Object();

    curPage = jefsFile.readFirstPage();

    nextPagePos = curPage.getNextPagePosition();
    curPageData = curPage.getData();

    curPageDataIdx = 0;
  }

  @Override
  public int read() throws IOException {
    synchronized (sLock) {
      int ascii = -1;
      byte b;

      if (closed) {
        throw new IOException("JEFS Input Stream is Closed!");
      }

      if (curPageDataIdx >= curPage.getUsedDataSize()) {
        if (nextPagePos >= 0) {
          curPageDataIdx = 0;
          curPageData = null;

          curPage = jefsFile.readPage(nextPagePos);

          if (curPage != null) {
            nextPagePos = curPage.getNextPagePosition();
            curPageData = curPage.getData();
          }
        }
      }

      if (curPageData != null && curPageDataIdx < curPage.getUsedDataSize()) {
        b = curPageData[curPageDataIdx];
        curPageDataIdx++;

        ascii = (b & 0xFF);
      }

      return ascii;
    }
  }

  @Override
  public void close() throws IOException {
    synchronized (sLock) {
      if (!closed) {
        super.close();
        closed = true;
      }
    }
  }

  @Override
  public String toString() {
    return "JefsInputStream [jefsFile=" + jefsFile + ", nextPagePos=" + nextPagePos + ", curPage=" + curPage + ", curPageDataIdx=" + curPageDataIdx + ", closed=" + closed + "]";
  }

}
