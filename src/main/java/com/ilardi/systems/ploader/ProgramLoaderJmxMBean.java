/**
 * Created Jun 25, 2024
 */
package com.ilardi.systems.ploader;

import com.ilardi.systems.util.IlardiSystemsException;

/**
 * @author robert.ilardi
 *
 */

public interface ProgramLoaderJmxMBean {
  public void stopProgram() throws IlardiSystemsException, InterruptedException;
}
