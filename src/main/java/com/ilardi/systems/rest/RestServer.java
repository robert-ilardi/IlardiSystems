/**
 * Created Jun 26, 2024
 */
package com.ilardi.systems.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.ilardi.IlardiSystemsException;
import io.ilardi.ploader.BaseLoadableProgram;
import io.ilardi.ploader.ProgramLoader;

/**
 * @Author robert.ilardi
 */

public class RestServer extends BaseLoadableProgram {

  private static final Logger logger = LogManager.getLogger(RestServer.class);

  /**
   * @param programAsync
   * @param programName
   * @param programLoader
   */
  public RestServer(String programName, ProgramLoader programLoader) {
    super(true, programName, programLoader);
  }

  @Override
  protected void runProgramInit() throws IlardiSystemsException {
    // TODO Auto-generated method stub

  }

  @Override
  protected void beginProgramExecution(String[] progArgs) throws IlardiSystemsException {
    // TODO Auto-generated method stub

  }

  @Override
  protected void endProgramExecution() throws IlardiSystemsException {
    // TODO Auto-generated method stub

  }

}
