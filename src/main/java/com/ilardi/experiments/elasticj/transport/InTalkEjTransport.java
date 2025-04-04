/**
 * Created Jan 31, 2021
 */
package com.ilardi.experiments.elasticj.transport;

import com.ilardi.experiments.elasticj.model.EjTransportMessage;
import com.ilardi.experiments.elasticj.util.EjException;
import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author rilardi
 *
 */

public class InTalkEjTransport extends BaseEjTransport {

  protected static final Logger logger = LogUtil.getInstance().getLogger(InTalkEjTransport.class);

  public InTalkEjTransport() {
    super();
  }

  @Override
  public void init() throws EjException {
    // TODO Auto-generated method stub

  }

  @Override
  public void shutdown() throws EjException {
    // TODO Auto-generated method stub

  }

  @Override
  public void send(EjTransportMessage message) throws EjException {
    // TODO Auto-generated method stub

  }

  @Override
  public void onReceive(EjTransportMessage message) throws EjException {
    // TODO Auto-generated method stub

  }

  @Override
  public EjTransportMessage sendWaitForReply(EjTransportMessage message) throws EjException {
    // TODO Auto-generated method stub
    return null;
  }

}
