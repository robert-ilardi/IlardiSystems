/**
 * Created Aug 26, 2024
 */
package com.refmatica;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ilardi.systems.IlardiSystemsException;
import com.ilardi.systems.ploader.BaseLoadableProgram;
import com.ilardi.systems.ploader.ProgramLoader;

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
  public void startProgram(String[] progArgs) throws IlardiSystemsException {
    // TODO Auto-generated method stub

  }

  @Override
  public void stopProgram() throws IlardiSystemsException {
    // TODO Auto-generated method stub

  }

}
