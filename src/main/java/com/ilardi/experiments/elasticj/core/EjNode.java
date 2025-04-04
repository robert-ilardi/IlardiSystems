/**
 * Created Jan 24, 2021
 */
package com.ilardi.experiments.elasticj.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Properties;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.ilardi.experiments.elasticj.Version;
import com.ilardi.experiments.elasticj.model.EjNodeInfo;
import com.ilardi.experiments.elasticj.util.EjException;
import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author rilardi
 *
 */

public class EjNode {

  protected static final Logger logger = LogUtil.getInstance().getLogger(EjNode.class);

  private static final String APP_TITLE = "ElasticJ Node";
  private static final String JMX_ROOT_NAME = "ElasticJ.Node";

  private EjNodeKernel kernel;

  private MBeanServer jmxServer;
  private ObjectName jmxObjName;
  private EjNodeJmxMBean ejnMb;

  private final Object nodeLock;

  private volatile boolean halted = false;

  private String nodePropertiesFile;

  private Properties nodeProperties;

  public EjNode() {
    nodeLock = new Object();
  }

  public void enableShutdownHook() {
    logger.info("Enabling Shutdown Hook");

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        try {
          logger.info("Running Shutdown Hook");
          EjNode.this.shutdown();
        }
        catch (Exception e) {
          logger.error(e);
        }
      }
    });
  }

  public void loadNodeProperties(String nodePropertiesFile) throws EjException {
    FileInputStream fis = null;

    try {
      logger.info("Loading Node Properties: " + nodePropertiesFile);

      this.nodePropertiesFile = nodePropertiesFile;

      fis = new FileInputStream(this.nodePropertiesFile);

      loadNodeProperties(fis);
    }
    catch (Exception e) {
      throw new EjException("An error occurred while attempting to Load Node Properties. System Message: " + e.getMessage(), e);
    }
    finally {
      try {
        if (fis != null) {
          fis.close();
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void loadNodeProperties(FileInputStream fis) throws IOException {
    Properties props = new Properties();

    props.load(fis);

    setNodeProperties(props);
  }

  public void boot() throws EjException {
    synchronized (nodeLock) {
      kernel = new EjNodeKernel();

      kernel.setNodeProperties(nodeProperties);

      kernel.boot();
    }
  }

  public void setNodeProperties(Properties nodeProperties) {
    this.nodeProperties = nodeProperties;
  }

  public void shutdown() throws EjException {
    synchronized (nodeLock) {
      if (!halted) {
        logger.info("Shutdown ElasticJ Node");

        if (kernel != null) {
          kernel.shutdown();
          kernel = null;
        }

        kernel = null;
        halted = true;
      }
    }
  }

  public void waitWhileNodeRunning() throws EjException {
    kernel.waitWhileNodeRunning();
  }

  public void setupJmx() throws EjException {
    StringBuilder sb;
    String tmp;
    EjNodeJmx ejnMbImpl;
    EjNodeInfo nodeInfo;

    try {
      logger.info("Setting Up JMX Management for ElasticJ Node");

      nodeInfo = kernel.getNodeInfo();

      sb = new StringBuilder();
      sb.append(JMX_ROOT_NAME);
      sb.append(":nodeName=");
      sb.append(nodeInfo.getNodeName());

      tmp = sb.toString();

      ejnMbImpl = new EjNodeJmx();
      ejnMbImpl.setEjNode(this);

      ejnMb = ejnMbImpl;

      jmxServer = ManagementFactory.getPlatformMBeanServer();
      jmxObjName = new ObjectName(tmp);

      jmxServer.registerMBean(ejnMb, jmxObjName);
    } // End try block
    catch (Exception e) {
      throw new EjException("An error occurred while attempting to Setup JMX. System Message: " + e.getMessage(), e);
    }
  }

  private static String getCheckArgMessage(int checkArgStatus) {
    StringBuilder sb = new StringBuilder();

    switch (checkArgStatus) {
      case 1:
        sb.append("Error: Command Line Argument [ELASTICJ_NODE_PROPERTIES_FILEPATH] NOT Set. Please check command line parameters and rerun program.\n");
        break;
      case 2:
        sb.append("Error: Command Line Argument [ELASTICJ_NODE_PROPERTIES_FILEPATH] CANNOT Be NULL. Please check command line parameters and rerun program.\n");
        break;
      case 3:
        sb.append("Error: Command Line Argument [ELASTICJ_NODE_PROPERTIES_FILEPATH] CANNOT be an Empty String (\"\"). Please check command line parameters and rerun program.\n");
        break;
    }

    sb.append("Usage: java ");
    sb.append(EjNode.class);
    sb.append(" [ELASTICJ_NODE_PROPERTIES_FILEPATH]");

    return sb.toString();
  }

  private static int checkCmdLineArgs(String[] args) {
    int status;

    if (args.length == 0) {
      status = 1;
    }
    else if (args[0] == null) {
      status = 2;
    }
    else if (args[0].trim().length() == 0) {
      status = 3;
    }
    else {
      status = 0;
    }

    return status;
  }

  public static void main(String[] args) {
    int exitCd, checkArgStatus;
    EjNode server = null;
    String filePath, errMesg, tmp;
    StringBuilder sb;

    checkArgStatus = checkCmdLineArgs(args);

    if (checkArgStatus != 0) {
      exitCd = 1;
      errMesg = getCheckArgMessage(checkArgStatus);
      System.err.println(errMesg);
      logger.error(errMesg);
    }
    else {
      try {
        sb = new StringBuilder();

        sb.append(APP_TITLE);
        sb.append("\n");

        tmp = Version.getVersionInfo();
        sb.append(tmp);

        tmp = sb.toString();
        System.out.println(tmp);
        logger.info(tmp);

        filePath = args[0];
        filePath = filePath.trim();

        server = new EjNode();

        server.loadNodeProperties(filePath);

        server.boot();

        server.setupJmx();

        server.enableShutdownHook();

        server.waitWhileNodeRunning();

        exitCd = 0;
      }
      catch (Exception e) {
        exitCd = 1;
        e.printStackTrace();
        logger.fatal(e);
      }
      finally {
        try {
          if (server != null) {
            server.shutdown();
          }
        }
        catch (Exception e) {
          e.printStackTrace();
          logger.error(e);
        }
      }
    }

    System.exit(exitCd);
  }

}
