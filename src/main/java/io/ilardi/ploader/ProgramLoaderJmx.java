/**
 * Created Jun 25, 2024
 */
package io.ilardi.ploader;

import io.ilardi.IlardiSystemsException;

/**
 * @author kilardi
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
