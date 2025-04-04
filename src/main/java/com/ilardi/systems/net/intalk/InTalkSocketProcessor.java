/**
 * Created Jan 24, 2021
 */
package com.ilardi.systems.net.intalk;

import java.nio.ByteBuffer;

import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;
import com.ilardi.systems.net.BaseSocketProcessor;
import com.ilardi.systems.net.IlardiNetException;
import com.ilardi.systems.net.SessionContext;

/**
 * @author rilardi
 *
 */

public abstract class InTalkSocketProcessor extends BaseSocketProcessor {

  protected static final Logger logger = LogUtil.getInstance().getLogger(InTalkSocketProcessor.class);

  public InTalkSocketProcessor() {
    super();
  }

  @Override
  public void init() throws IlardiNetException {
    logger.info("InTalkSocketProcessor Executing Init");
  }

  @Override
  public void shutdown() throws IlardiNetException {
    logger.info("InTalkSocketProcessor Executing Shutdown");
  }

  @Override
  public void process(SessionContext sessionContext) throws IlardiNetException {
    CommandDataPair cdp;

    cdp = getNextCdp(sessionContext);

    while (cdp != null) {
      process(sessionContext, cdp);

      cdp = getNextCdp(sessionContext);
    }
  }

  private CommandDataPair getNextCdp(SessionContext sessionContext) throws IlardiNetException {
    CommandDataPair cdp = null;
    byte[] header, data;
    int dataLen;

    header = sessionContext.dequeueData(CommandDataPair.HEADER_LEN);

    if (header != null) {
      dataLen = getDataLengthFromHeader(header);

      data = sessionContext.dequeueData(dataLen);

      if (data != null) {
        cdp = new CommandDataPair(header, data);
      }
    }

    return cdp;
  }

  private int getDataLengthFromHeader(byte[] header) {
    int dataLen = -1;
    ByteBuffer bb;

    if (header != null && header.length == CommandDataPair.HEADER_LEN) {
      bb = ByteBuffer.wrap(header, CommandDataPair.HEADER_POS_DATA_LEN, 4);
      dataLen = bb.getInt();
    }

    return dataLen;
  }

  protected abstract void process(SessionContext sessionContext, CommandDataPair cdp) throws IlardiNetException;

}
