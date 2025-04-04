/**
 * Created Jul 28, 2021
 */
package com.ilardi.systems.jefs;

import java.io.IOException;

/**
 * @author robert.ilardi
 *
 */

public class JefsRandomAccessFile {

  private final JefsFile jefsFile;

  private final Object fLock;

  protected int pageDataSize;

  protected long nextPagePos;

  protected JefsPage curPage;
  protected byte[] curPageData;
  protected int curPageDataIdx;

  protected boolean closed;

  protected boolean writePending;

  public JefsRandomAccessFile(JefsFile jefsFile) throws IOException {
    this.jefsFile = jefsFile;

    closed = false;

    fLock = new Object();

    curPageDataIdx = 0;

    writePending = false;

    pageDataSize = jefsFile.getFileSystemInfoPageDataSize();

    curPage = jefsFile.readFirstPage();

    nextPagePos = curPage.getNextPagePosition();
    curPageData = curPage.getData();

    if (curPageData == null) {
      curPageData = new byte[pageDataSize];
      curPage.setData(curPageData);
    }
  }

  @Override
  public String toString() {
    return "JefsRandomAccessFile [jefsFile=" + jefsFile + ", pageDataSize=" + pageDataSize + ", nextPagePos=" + nextPagePos + ", curPage=" + curPage + ", curPageDataIdx=" + curPageDataIdx
        + ", closed=" + closed + ", writePending=" + writePending + "]";
  }

  public long size() throws IOException {
    synchronized (fLock) {
      long sz;

      if (closed) {
        throw new IOException("JEFS Random Access File is Closed!");
      }

      sz = jefsFile.size();

      return sz;
    }
  }

  public void flush() throws IOException {
    synchronized (fLock) {
      if (!closed) {
        if (writePending) {
          jefsFile.writePage(curPage);
          writePending = false;
        }
      }
    }
  }

  public void close() throws IOException {
    synchronized (fLock) {
      if (!closed) {
        flush();
        closed = true;
      }
    }
  }

  public void seek(long position) throws IOException {
    synchronized (fLock) {
      long sz, pageIndex, nextPagePos;
      int tmpPageDataIdx;

      if (closed) {
        throw new IOException("JEFS Random Access File is Closed!");
      }

      flush();

      sz = size();

      if (position < 0) {
        throw new IOException("Seek Position Cannot be Less than ZERO!");
      }

      if (position < sz) {
        pageIndex = position / pageDataSize;

        curPage = jefsFile.readFirstPage();

        for (int i = 0; i < pageIndex; i++) {
          nextPagePos = curPage.getNextPagePosition();

          if (nextPagePos <= 0) {
            throw new IOException("Seek Passed EOF");
          }

          curPage = jefsFile.readPage(nextPagePos);
        }

        tmpPageDataIdx = (int) (position - (pageIndex * pageDataSize));

        if (tmpPageDataIdx >= curPage.getUsedDataSize()) {
          throw new IOException("Seek Passed EOF");
        }

        curPageDataIdx = tmpPageDataIdx;
        curPageData = curPage.getData();
      }
      else {
        throw new IOException("Seek Passed EOF");
      }
    }
  }

  public int read() throws IOException {
    synchronized (fLock) {
      int ascii = -1;
      byte b;

      if (closed) {
        throw new IOException("JEFS Random Access File is Closed!");
      }

      flush();

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

  public void write(int b) throws IOException {
    synchronized (fLock) {
      if (closed) {
        throw new IOException("JEFS Random Access File is Closed!");
      }

      curPageData[curPageDataIdx] = ((byte) (b <= 127 ? b : (b - 256)));
      curPageDataIdx++;

      if (curPageDataIdx > curPage.getUsedDataSize()) {
        curPage.setUsedDataSize(curPageDataIdx);
      }

      if (curPageDataIdx >= pageDataSize) {
        flush();

        curPage = jefsFile.appendEmptyPage(curPage);

        curPageDataIdx = 0;
        curPageData = curPage.getData();

        if (curPageData == null) {
          curPageData = new byte[pageDataSize];
          curPage.setData(curPageData);
        }
      }

      writePending = true;
    }
  }

  public void write(byte[] data) throws IOException {
    synchronized (fLock) {
      if (closed) {
        throw new IOException("JEFS Random Access File is Closed!");
      }

      write(data, 0, data.length);
    }
  }

  public void write(byte[] data, int off, int len) throws IOException {
    synchronized (fLock) {
      int ascii;
      byte b;

      if (closed) {
        throw new IOException("JEFS Random Access File is Closed!");
      }

      for (int i = off; i < len; i++) {
        b = data[i];

        ascii = (b & 0xFF);

        write(ascii);
      }
    }
  }

  public int read(byte[] buf) throws IOException {
    synchronized (fLock) {
      int ascii, readLen = -1;
      byte b;

      if (closed) {
        throw new IOException("JEFS Random Access File is Closed!");
      }

      for (int i = 0; i < buf.length; i++) {
        ascii = read();

        if (ascii == -1) {
          break;
        }

        b = ((byte) (ascii <= 127 ? ascii : (ascii - 256)));

        buf[i] = b;

        if (readLen == -1) {
          readLen = 1;
        }
        else {
          readLen++;
        }
      }

      return readLen;
    }
  }

}
