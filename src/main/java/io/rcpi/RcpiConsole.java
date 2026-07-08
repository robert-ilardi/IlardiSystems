/**
 * Created Jul 7, 2026
 */
package io.rcpi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.ilardi.IlardiSystemsException;
import io.ilardi.ploader.BaseLoadableProgram;
import io.ilardi.ploader.ProgramLoader;

/**
 * @author Kate Ilardi
 */

public class RcpiConsole extends BaseLoadableProgram {

  private static final Logger logger = LogManager.getLogger(RcpiConsole.class);

  /**
   * @param programAsync
   * @param programName
   * @param programLoader
   */
  public RcpiConsole(String programName, ProgramLoader programLoader) {
    super(true, programName, programLoader);
  }

  @Override
  protected void runProgramInit() throws IlardiSystemsException {
    logger.debug("Running Program Initialization: " + getProgramName());
  }

  @Override
  protected void beginProgramExecution(String[] progArgs) throws IlardiSystemsException {
    logger.debug("Begin Program Execution: " + getProgramName());
  }

  @Override
  public void endProgramExecution() throws IlardiSystemsException {
    logger.debug("End Program Execution: " + getProgramName());
  }

}
