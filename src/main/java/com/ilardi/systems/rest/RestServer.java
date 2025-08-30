/**
 * Created Jun 26, 2024
 */
package com.ilardi.systems.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ilardi.systems.IlardiSystemsException;

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
  public void startProgram(String[] progArgs) throws IlardiSystemsException {
    // TODO Auto-generated method stub

  }

  @Override
  public void stopProgram() throws IlardiSystemsException {
    // TODO Auto-generated method stub

  }

}
