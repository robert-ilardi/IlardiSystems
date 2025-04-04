/**
 * Created Jan 16, 2021
 */
package com.ilardi.systems.net.client;

import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;
import com.ilardi.systems.net.BaseSocketThreader;
import com.ilardi.systems.net.BlockingThreadSocketProcessorContainer;
import com.ilardi.systems.net.IlardiNetException;
import com.ilardi.systems.net.SessionContext;
import com.ilardi.systems.net.SocketProcessor;

/**
 * @author rilardi
 *
 */

public class BlockingSocketClientThreader extends BaseSocketThreader {

  protected static final Logger logger = LogUtil.getInstance().getLogger(BlockingSocketClientThreader.class);

  private BlockingThreadSocketProcessorContainer processorContainer;

  public BlockingSocketClientThreader() {
    super();
  }

  @Override
  public void init() throws IlardiNetException {
    synchronized (threaderLock) {
      logger.info("BlockingSocketClientThreader Executing Init");
    }
  }

  @Override
  public void destroy() throws IlardiNetException {
    synchronized (threaderLock) {
      logger.info("BlockingSocketClientThreader Executing Shutdown");

      if (processorContainer != null) {
        stopProcessorContainer();
      }
    }
  }

  private void stopProcessorContainer() throws IlardiNetException {
    synchronized (threaderLock) {
      logger.info("BlockingSocketClientThreader Executing stopProcessorContainer");

      if (processorContainer != null) {
        processorContainer.shutdown();
        processorContainer = null;
      }
    }
  }

  @Override
  public void onConnect(SessionContext clientContext) throws IlardiNetException {
    synchronized (threaderLock) {
      String sessionId = clientContext.getSessionId();

      String tmp = (new StringBuilder()).append("Starting Processing Container for Session ").append(sessionId).toString();
      logger.info(tmp);

      processorContainer = createProcessorContainer(clientContext);
    }
  }

  private BlockingThreadSocketProcessorContainer createProcessorContainer(SessionContext clientContext) throws IlardiNetException {
    BlockingThreadSocketProcessorContainer processorContainer;
    SocketProcessor processor;

    processor = createSocketProcessor();

    processorContainer = new BlockingThreadSocketProcessorContainer();

    processorContainer.setClientContext(clientContext);
    processorContainer.setSocketThreader(this);
    processorContainer.setSocketProcessor(processor);

    processorContainer.init();

    return processorContainer;
  }

  private SocketProcessor createSocketProcessor() throws IlardiNetException {
    SocketProcessor processor;

    processor = socketFramework.createSocketProcessor();

    return processor;
  }

  @Override
  public void onDisconnect(SessionContext clientContext) throws IlardiNetException {
    String sessionId = clientContext.getSessionId();

    String tmp = (new StringBuilder()).append("Threader Executing onDisconnect for Session = ").append(sessionId).toString();
    logger.info(tmp);

    stopProcessorContainer();
  }

}
