/**
 * Created Jan 16, 2021
 */
package com.ilardi.systems.net.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

public class BlockingSocketServerPerClientThreader extends BaseSocketThreader {

  protected static final Logger logger = LogUtil.getInstance().getLogger(BlockingSocketServerPerClientThreader.class);

  private HashMap<String, BlockingThreadSocketProcessorContainer> processorContainers;

  public BlockingSocketServerPerClientThreader() {
    super();

    processorContainers = new HashMap<String, BlockingThreadSocketProcessorContainer>();
  }

  @Override
  public void init() throws IlardiNetException {
    synchronized (threaderLock) {
      logger.info("BlockingSocketServerPerClientThreader Executing Init");
    }
  }

  @Override
  public void destroy() throws IlardiNetException {
    synchronized (threaderLock) {
      logger.info("BlockingSocketServerPerClientThreader Executing Shutdown");

      if (processorContainers != null) {
        stopAllProcessorContainers();

        processorContainers.clear();
        processorContainers = null;
      }
    }
  }

  private void stopAllProcessorContainers() {
    synchronized (threaderLock) {
      logger.info("Stopping ALL Processor Containers");

      ArrayList<String> keysCopy = new ArrayList<String>();
      Iterator<String> iter = processorContainers.keySet().iterator();

      while (iter.hasNext()) {
        String sessionId = iter.next();

        keysCopy.add(sessionId);
      }

      for (String key : keysCopy) {
        try {
          stopProcessorContainer(key);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void stopProcessorContainer(String sessionId) throws IlardiNetException {
    synchronized (threaderLock) {
      String tmp = (new StringBuilder()).append("Stopping Processing Container for Session ").append(sessionId).toString();
      logger.info(tmp);

      BlockingThreadSocketProcessorContainer processorContainer = processorContainers.get(sessionId);

      stopProcessorContainer(sessionId, processorContainer);
    }
  }

  private void stopProcessorContainer(String sessionId, BlockingThreadSocketProcessorContainer processorContainer) throws IlardiNetException {
    synchronized (threaderLock) {
      processorContainer.shutdown();

      processorContainers.remove(sessionId);
    }
  }

  @Override
  public void onConnect(SessionContext clientContext) throws IlardiNetException {
    synchronized (threaderLock) {
      String sessionId = clientContext.getSessionId();

      String tmp = (new StringBuilder()).append("Starting Processing Container for Session ").append(sessionId).toString();
      logger.info(tmp);

      BlockingThreadSocketProcessorContainer processorContainer = createProcessorContainer(clientContext);

      processorContainers.put(sessionId, processorContainer);
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

    stopProcessorContainer(sessionId);
  }

}
