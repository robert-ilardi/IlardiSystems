/**
 * Created Jun 17, 2024
 */
package com.ilardi.systems.ploader;

import com.ilardi.systems.IlardiSystemsException;

/**
 * @author rober
 */

public interface LoadableProgram {
  public void init() throws IlardiSystemsException;

  public void destroy() throws IlardiSystemsException;

  public boolean isProgramInited();

  public boolean isProgramStarting();

  public boolean isProgramStartSuccessful();

  public boolean isProgramRunning();

  public boolean isProgramAsync();

  public void setProgramInited(boolean programInited);

  public void setProgramStarting(boolean programStarting);

  public void setProgramRunning(boolean programRunning);

  public void setProgramStartSuccessful(boolean programStartSuccessful);

  public void startProgram(String[] progArgs) throws IlardiSystemsException;

  public void stopProgram() throws IlardiSystemsException;

  public void waitWhileProgramRunning() throws IlardiSystemsException, InterruptedException;

  public void waitWhileProgramStarting() throws IlardiSystemsException, InterruptedException;
}
