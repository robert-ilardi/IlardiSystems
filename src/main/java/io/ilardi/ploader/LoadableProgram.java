/**
 * Created Jun 17, 2024
 */
package io.ilardi.ploader;

import io.ilardi.IlardiException;

/**
 * @author rober
 */

public interface LoadableProgram {
  public void init() throws IlardiException;

  public void destroy() throws IlardiException;

  public boolean isProgramInited();

  public boolean isProgramStarting();

  public boolean isProgramStartSuccessful();

  public boolean isProgramRunning();

  public boolean isProgramAsync();

  public void setProgramInited(boolean programInited);

  public void setProgramStarting(boolean programStarting);

  public void setProgramRunning(boolean programRunning);

  public void setProgramStartSuccessful(boolean programStartSuccessful);

  public void startProgram(String[] progArgs) throws IlardiException;

  public void stopProgram() throws IlardiException;

  public void waitWhileProgramRunning() throws IlardiException, InterruptedException;

  public void waitWhileProgramStarting() throws IlardiException, InterruptedException;
}
