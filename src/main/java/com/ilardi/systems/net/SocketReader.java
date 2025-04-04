/**
 * Created Jan 9, 2021
 */
package com.ilardi.systems.net;

/**
 * @author rilardi
 *
 */

public interface SocketReader {

  public void init() throws IlardiNetException;

  public void close() throws IlardiNetException;

  public byte[] read() throws IlardiNetException;

  public void interruptReader() throws IlardiNetException;

}
