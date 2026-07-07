/**
 * Created Jun 25, 2024
 */
package io.ilardi.ploader;

import io.ilardi.IlardiSystemsException;

/**
 * @author kilardi
 *
 */

public interface ProgramLoaderJmxMBean {
  public void stopProgram() throws IlardiSystemsException, InterruptedException;
}
