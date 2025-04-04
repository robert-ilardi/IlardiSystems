/**
 * Created Jun 25, 2024
 */
package com.ilardi.systems.ploader;

import com.ilardi.systems.util.IlardiSystemsException;

/**
 * @author robert.ilardi
 *
 */

public class ProgramLoaderJmx implements ProgramLoaderJmxMBean {

  protected ProgramLoader programLoader;
  protected LoadableProgram program;

  public void setProgramLoader(ProgramLoader programLoader) {
    this.programLoader = programLoader;
  }

  public void setProgram(LoadableProgram program) {
    this.program = program;
  }

  @Override
  public void stopProgram() throws IlardiSystemsException, InterruptedException {
    program.stopProgram();
  }

}
