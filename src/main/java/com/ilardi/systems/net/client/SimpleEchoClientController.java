/**
 * Created Jan 23, 2021
 */
package com.ilardi.systems.net.client;

import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;
import com.ilardi.systems.net.BaseSocketFrameworkController;
import com.ilardi.systems.net.IlardiNetException;
import com.ilardi.systems.net.SessionContext;

/**
 * @author rilardi
 *
 */

public class SimpleEchoClientController extends BaseSocketFrameworkController {

  protected static final Logger logger = LogUtil.getInstance().getLogger(SimpleEchoClientController.class);

  public SimpleEchoClientController() {
    super();
  }

  @Override
  public void init() throws IlardiNetException {
    logger.info("SimpleEchoClientController Executing Init");
  }

  @Override
  public void destroy() throws IlardiNetException {
    logger.info("SimpleEchoClientController Executing Destroy");
  }

  @Override
  public void onConnect(SessionContext clientContext) throws IlardiNetException {
    logger.info("SimpleEchoClientController Executing onConnect");

    sendRandomData(clientContext);
  }

  @Override
  public void onDisconnect(SessionContext clientContext) throws IlardiNetException {
    logger.info("SimpleEchoClientController Executing onDisconnect");
  }

  @Override
  public void onData(SessionContext clientContext) throws IlardiNetException {
    sendRandomData(clientContext);
  }

  private void sendRandomData(SessionContext clientContext) throws IlardiNetException {
    String str;
    byte[] data;

    str = generateRandomString();

    data = str.getBytes();

    clientContext.write(data);
  }

  private String generateRandomString() {
    StringBuilder sb = new StringBuilder();
    String tmp;
    int len;

    len = ((int) (256 * Math.random())) + 1;

    for (int i = 0; i < len; i++) {
      if (((int) (100 * Math.random())) >= 50) {
        // Numbers
        sb.append((char) (48 + ((int) (10 * Math.random()))));
      }
      else {
        // Letters
        sb.append((char) (65 + ((int) (26 * Math.random()))));
      }
    }

    tmp = sb.toString();

    return tmp;
  }

}
