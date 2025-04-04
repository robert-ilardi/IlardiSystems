/**
 * Created Jan 24, 2021
 */
package com.ilardi.systems.net.intalk;

import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;
import com.ilardi.systems.net.IlardiNetException;
import com.ilardi.systems.net.SessionContext;
import com.ilardi.systems.net.client.SocketClient;

/**
 * @author rilardi
 *
 */

public class DebugInTalkSocketProcessor extends InTalkSocketProcessor {

  protected static final Logger logger = LogUtil.getInstance().getLogger(SocketClient.class);

  public DebugInTalkSocketProcessor() {
    super();
  }

  @Override
  protected void process(SessionContext sessionContext, CommandDataPair cdp) throws IlardiNetException {
    String tmp = (new StringBuilder()).append("InTalk Protocol Socket Processor for Session = ").append(sessionContext.getSessionId()).append(" Processing CommandDataPair = ").append(cdp).toString();
    logger.info(tmp);
  }

}
