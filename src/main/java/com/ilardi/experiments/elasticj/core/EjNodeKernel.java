/**
 * Created Jan 24, 2021
 */
package com.ilardi.experiments.elasticj.core;

import java.util.Properties;

import com.ilardi.experiments.elasticj.model.EjNodeInfo;
import com.ilardi.experiments.elasticj.model.EjNodeType;
import com.ilardi.experiments.elasticj.util.EjException;
import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author rilardi
 *
 */

public class EjNodeKernel {

  protected static final Logger logger = LogUtil.getInstance().getLogger(EjNodeKernel.class);

  protected static final String PROP_NODE_NAME = "ElasticJ.NodeName";
  protected static final String PROP_NODE_TYPE = "ElasticJ.NodeType";
  protected static final String PROP_TRANSPORT_CLASSNAME = "ElasticJ.TransportClassname";

  private final Object kernelLock;

  private Properties nodeProperties;

  private volatile boolean booted = false;

  private EjNodeInfo nodeInfo;

  private EjProcessTable processTable;

  public EjNodeKernel() {
    kernelLock = new Object();
  }

  public void setNodeProperties(Properties nodeProperties) {
    this.nodeProperties = nodeProperties;
  }

  public EjNodeInfo getNodeInfo() {
    return nodeInfo;
  }

  public void boot() throws EjException {
    synchronized (kernelLock) {
      if (booted) {
        return;
      }

      logger.info("Booting ElasticJ Node Kernel");

      nodeInfo = createNodeInfo();

      processTable = createProcessTable();

      booted = true;
      kernelLock.notifyAll();
    }
  }

  public void shutdown() throws EjException {
    synchronized (kernelLock) {
      if (!booted) {
        return;
      }

      logger.info("Shutting Down ElasticJ Node Kernel");

      // TODO Auto-generated method stub

      nodeInfo = null;

      booted = false;
      kernelLock.notifyAll();
    }
  }

  public void waitWhileNodeRunning() throws EjException {
    logger.info("Waiting while Node is Running");

    // TODO Auto-generated method stub
  }

  protected EjNodeInfo createNodeInfo() throws EjException {
    EjNodeInfo nodeInfo;
    String tmp;
    EjNodeType nodeType;

    nodeInfo = new EjNodeInfo();
    nodeInfo.setNodeProperties(nodeProperties);

    tmp = nodeProperties.getProperty(PROP_NODE_NAME);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        nodeInfo.setNodeName(tmp);
      }
      else {
        throw new EjException("Node Name NOT Set! Please Check ElasticJ Node Properties.");
      }
    }
    else {
      throw new EjException("Node Name NOT Set! Please Check ElasticJ Node Properties.");
    }

    tmp = nodeProperties.getProperty(PROP_NODE_TYPE);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        nodeType = EjNodeType.valueOf(tmp);
        nodeInfo.setNodeType(nodeType);
      }
      else {
        throw new EjException("Node Type NOT Set! Please Check ElasticJ Node Properties.");
      }
    }
    else {
      throw new EjException("Node Type NOT Set! Please Check ElasticJ Node Properties.");
    }

    tmp = nodeProperties.getProperty(PROP_TRANSPORT_CLASSNAME);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        nodeInfo.setTransportClassname(tmp);
      }
      else {
        throw new EjException("Transport Classname NOT Set! Please Check ElasticJ Node Properties.");
      }
    }
    else {
      throw new EjException("Transport Classname NOT Set! Please Check ElasticJ Node Properties.");
    }

    return nodeInfo;
  }

  protected EjProcessTable createProcessTable() {
    EjProcessTable processTable = null;

    return processTable;
  }

}
