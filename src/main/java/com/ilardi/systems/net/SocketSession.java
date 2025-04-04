/**
 * Created Jan 8, 2021
 */
package com.ilardi.systems.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author rilardi
 *
 */

public class SocketSession {

  private HashMap<String, Object> sessionParameters;

  private ArrayList<byte[]> dataQueue;

  private final Object sessionLock;

  public SocketSession() {
    sessionLock = new Object();
    sessionParameters = new HashMap<String, Object>();
    dataQueue = new ArrayList<byte[]>();
  }

  public void enqueueData(byte[] data) {
    synchronized (sessionLock) {
      dataQueue.add(data);
    }
  }

  public Object getSessionParameter(String parameterName) {
    synchronized (sessionLock) {
      return sessionParameters.get(parameterName);
    }
  }

  public Object putSessionParameter(String parameterName, Object parameterValue) {
    synchronized (sessionLock) {
      return sessionParameters.put(parameterName, parameterValue);
    }
  }

  public Object removeSessionParameter(String parameterName) {
    synchronized (sessionLock) {
      return sessionParameters.remove(parameterName);
    }
  }

  public boolean hasSessionParameter(String parameterName) {
    synchronized (sessionLock) {
      return sessionParameters.containsKey(parameterName);
    }
  }

  public void clearSessionParameters() {
    synchronized (sessionLock) {
      sessionParameters.clear();
    }
  }

  public byte[] dequeueData() {
    synchronized (sessionLock) {
      byte[] data = null;

      if (!dataQueue.isEmpty()) {
        data = dataQueue.remove(0);
      }

      return data;
    }
  }

  public boolean isDataQueueEmpty() {
    synchronized (sessionLock) {
      return dataQueue.isEmpty();
    }
  }

  public byte[] dequeueAllData() {
    synchronized (sessionLock) {
      byte[] retData = null, nextData, oldData;
      int nextLen, oldLen, retLen = 0;
      Iterator<byte[]> iter;

      if (!dataQueue.isEmpty()) {
        iter = dataQueue.iterator();

        while (iter.hasNext()) {
          nextData = iter.next();

          nextLen = nextData.length;
          oldLen = retLen;
          retLen += nextLen;

          if (retData == null) {
            retData = nextData;
          }
          else {
            oldData = retData;

            retData = new byte[retLen];

            System.arraycopy(oldData, 0, retData, 0, oldLen);
            System.arraycopy(nextData, oldLen, retData, oldLen, nextLen);
          }

          iter.remove();
        }
      }

      return retData;
    }
  }

  public void destroy() {
    synchronized (sessionLock) {
      if (sessionParameters != null) {
        clearSessionParameters();
        sessionParameters = null;
      }

      if (dataQueue != null) {
        dataQueue.clear();
        dataQueue = null;
      }
    }
  }

  public void pushData(byte[] data) {
    synchronized (sessionLock) {
      dataQueue.add(0, data);
    }
  }

  public byte[] dequeueData(int len) {
    synchronized (sessionLock) {
      byte[] data = null, tmp, tmp2, tmp3;

      while (!dataQueue.isEmpty()) {
        tmp = dataQueue.remove(0);

        if (tmp.length == len) {
          data = tmp;
          break;
        }
        else if (tmp.length > len) {
          data = new byte[len];
          System.arraycopy(tmp, 0, data, 0, len);

          tmp2 = new byte[tmp.length - len];
          System.arraycopy(tmp, len, tmp2, 0, tmp2.length);

          dataQueue.add(0, tmp2);

          break;
        }
        else if (!dataQueue.isEmpty()) {
          tmp2 = dataQueue.remove(0);

          tmp3 = new byte[tmp.length + tmp2.length];

          System.arraycopy(tmp, 0, tmp3, 0, tmp.length);
          System.arraycopy(tmp2, 0, tmp3, tmp.length, tmp2.length);

          dataQueue.add(0, tmp3);
        }

        tmp = null;
        tmp2 = null;
        tmp3 = null;
      }

      return data;
    }
  }

}
