/**
 * Created Jun 25, 2024
 */
package io.ilardi.ploader;

import io.ilardi.IlardiException;

/**
 * @author robert.ilardi
 *
 */

public interface ProgramLoaderJmxMBean {
  public void stopProgram() throws IlardiException, InterruptedException;
}
