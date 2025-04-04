/**
 * Created Jan 9, 2021
 */
package com.ilardi.systems.net;

/**
 * @author rilardi
 *
 */

public abstract class BaseSocketWriter implements SocketWriter {

  protected final Object writerLock;

  public BaseSocketWriter() {
    writerLock = new Object();
  }

}
