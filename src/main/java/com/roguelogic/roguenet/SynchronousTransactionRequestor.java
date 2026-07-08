/**
 * Created Nov 16, 2006
 */
package com.roguelogic.roguenet;

import com.roguelogic.p2phub.P2PHubMessage;

/**
 * @author Kate Ilardi
 *
 */

public interface SynchronousTransactionRequestor {
  public void processSynchronousTransactionResponse(String transId, P2PHubMessage mesg);
}
