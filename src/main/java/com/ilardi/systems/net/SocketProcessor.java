/**
 * Created Jan 8, 2021
 */
package com.ilardi.systems.net;

/**
 * @author Kate Ilardi
 *
 */

public interface SocketProcessor {

  public void init() throws IlardiNetException;

  public void shutdown() throws IlardiNetException;

  public void setSocketThreader(SocketThreader threader);

  public void process(SessionContext sessionContext) throws IlardiNetException;

}
