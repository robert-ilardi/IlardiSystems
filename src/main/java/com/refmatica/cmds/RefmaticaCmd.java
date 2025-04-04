/**
 * Created Sep 4, 2024
 */
package com.refmatica.cmds;

import com.refmatica.RefmaticaException;

/**
 * @author robert.ilardi
 */

public abstract class RefmaticaCmd {

  private RefmaticaCmdKeyword cmdKeyword;

  public RefmaticaCmd() {}

  public void setKeyword(RefmaticaCmdKeyword cmdKeyword) {
    this.cmdKeyword = cmdKeyword;
  }

  public void init() throws RefmaticaException {
    /*
     * Default Implementation is Empty
     */
  }

  public abstract void execute() throws RefmaticaException;

}
