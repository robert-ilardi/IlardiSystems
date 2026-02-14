/**
 * Created Aug 26, 2024
 */
package com.refmatica;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.ilardi.IlardiSystemsException;
import io.ilardi.ploader.BaseLoadableProgram;
import io.ilardi.ploader.ProgramLoader;

/**
 * @author robert.ilardi
 */

public class RefmaticaServer extends BaseLoadableProgram {

  private static final Logger logger = LogManager.getLogger(RefmaticaServer.class);

  /**
   * @param programName
   * @param programLoader
   */
  public RefmaticaServer(String programName, ProgramLoader programLoader) {
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
