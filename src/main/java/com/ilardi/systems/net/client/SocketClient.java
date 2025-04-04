/**
 * Created Jan 15, 2021
 */
package com.ilardi.systems.net.client;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;
import com.ilardi.systems.net.IlardiNetException;
import com.ilardi.systems.net.Version;

/**
 * @author rilardi
 *
 */

public class SocketClient extends BaseSocketClient {

  protected static final Logger logger = LogUtil.getInstance().getLogger(SocketClient.class);

  private static final String APP_TITLE = "IlardiNet Socket Client";
  private static final String JMX_ROOT_NAME = "IlardiNet.SocketClient";

  private MBeanServer jmxServer;
  private ObjectName jmxObjName;
  private SocketClientJmxMBean scmb;

  private volatile boolean destroyed = false;

  public SocketClient() {
    super();
  }

  private static String getCheckArgMessage(int checkArgStatus) {
    StringBuilder sb = new StringBuilder();

    switch (checkArgStatus) {
      case 1:
        sb.append("Error: Command Line Argument [SOCKET_CLIENT_PROPERTIES_FILEPATH] NOT Set. Please check command line parameters and rerun program.\n");
        break;
      case 2:
        sb.append("Error: Command Line Argument [SOCKET_CLIENT_PROPERTIES_FILEPATH] CANNOT Be NULL. Please check command line parameters and rerun program.\n");
        break;
      case 3:
        sb.append("Error: Command Line Argument [SOCKET_CLIENT_PROPERTIES_FILEPATH] CANNOT be an Empty String (\"\"). Please check command line parameters and rerun program.\n");
        break;
    }

    sb.append("Usage: java ");
    sb.append(SocketClient.class);
    sb.append(" [SOCKET_CLIENT_PROPERTIES_FILEPATH]");

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

  public void enableShutdownHook() {
    logger.info("Enabling Shutdown Hook");

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        try {
          logger.info("Running Shutdown Hook");
          SocketClient.this.destroy();
        }
        catch (IlardiNetException e) {
          logger.error(e);
        }
      }
    });
  }

  @Override
  public void destroy() throws IlardiNetException {
    synchronized (frameworkLock) {
      if (!destroyed) {
        logger.info("Destroy Socket Client");
        super.destroy();
        destroyed = true;
      }
    }
  }

  public void setupJmx() throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
    StringBuilder sb;
    String tmp;
    SocketClientJmx scmbImpl;

    logger.info("Setting Up JMX Management for Socket Client");

    sb = new StringBuilder();
    sb.append(JMX_ROOT_NAME);
    sb.append(":port=");
    sb.append(socketInfo.getPort());

    tmp = sb.toString();

    scmbImpl = new SocketClientJmx();
    scmbImpl.setSocketClient(this);

    scmb = scmbImpl;

    jmxServer = ManagementFactory.getPlatformMBeanServer();
    jmxObjName = new ObjectName(tmp);

    jmxServer.registerMBean(scmb, jmxObjName);
  }

  public static void main(String[] args) {
    int exitCd, checkArgStatus;
    SocketClient client = null;
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

        client = new SocketClient();

        client.loadSocketFrameworkProperties(filePath);

        client.init();

        client.setupJmx();

        client.startSocketHandling();

        client.enableShutdownHook();

        client.waitWhileIsHandlingSockets();

        exitCd = 0;
      }
      catch (Exception e) {
        exitCd = 1;
        e.printStackTrace();
        logger.fatal(e);
      }
      finally {
        try {
          if (client != null) {
            client.destroy();
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
