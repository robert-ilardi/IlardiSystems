/**
 * Created Jan 31, 2021
 */
package com.ilardi.experiments.elasticj.transport;

import com.ilardi.experiments.elasticj.model.EjNodeInfo;
import com.ilardi.experiments.elasticj.model.EjTransportMessage;
import com.ilardi.experiments.elasticj.util.EjException;

/**
 * @author rilardi
 *
 */

public interface EjTransport {

  public void setNodeInfo(EjNodeInfo nodeInfo);

  public void init() throws EjException;

  public void shutdown() throws EjException;

  public void send(EjTransportMessage message) throws EjException;

  public void onReceive(EjTransportMessage message) throws EjException;

  public EjTransportMessage sendWaitForReply(EjTransportMessage message) throws EjException;

}
