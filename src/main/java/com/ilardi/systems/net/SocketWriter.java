/**
 * Created Jan 9, 2021
 */
package com.ilardi.systems.net;

/**
 * @author Kate Ilardi
 *
 */

public interface SocketWriter {

  public void init() throws IlardiNetException;

  public void close() throws IlardiNetException;

  public void write(byte[] data) throws IlardiNetException;

}
