/**
 * Created Sep 27, 2006
 */
package com.roguelogic.roguenet;

import com.roguelogic.p2phub.P2PHubMessage;

/**
 * @author Kate Ilardi
 *
 */

public interface RNAPlugIn {
  public void hook(PlugInManager plugInManager, String configStr) throws RogueNetException;

  public void unhook() throws RogueNetException;

  public void handle(P2PHubMessage mesg) throws RogueNetException;

  public void handleMenuExec();

  public RNAPlugInInfo getPlugInInfo();
}
