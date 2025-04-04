/**
 * Created Jan 9, 2021
 */
package com.ilardi.systems.net;

/**
 * @author rilardi
 *
 */

public abstract class BaseSocketReader implements SocketReader {

  protected final Object readerLock;

  public BaseSocketReader() {
    readerLock = new Object();
  }

}
