/**
 * Created Jan 15, 2021
 */
package com.ilardi.systems.net.server;

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

public class SocketServer extends BaseSocketServer {

  protected static final Logger logger = LogUtil.getInstance().getLogger(SocketServer.class);

  private static final String APP_TITLE = "IlardiNet Socket Server";
  private static final String JMX_ROOT_NAME = "IlardiNet.SocketServer";

  private MBeanServer jmxServer;
  private ObjectName jmxObjName;
  private SocketServerJmxMBean ssmb;

  private volatile boolean destroyed = false;

  public SocketServer() {
    super();
  }

  private static String getCheckArgMessage(int checkArgStatus) {
    StringBuilder sb = new StringBuilder();

    switch (checkArgStatus) {
      case 1:
        sb.append("Error: Command Line Argument [SOCKET_SERVER_PROPERTIES_FILEPATH] NOT Set. Please check command line parameters and rerun program.\n");
        break;
      case 2:
        sb.append("Error: Command Line Argument [SOCKET_SERVER_PROPERTIES_FILEPATH] CANNOT Be NULL. Please check command line parameters and rerun program.\n");
        break;
      case 3:
        sb.append("Error: Command Line Argument [SOCKET_SERVER_PROPERTIES_FILEPATH] CANNOT be an Empty String (\"\"). Please check command line parameters and rerun program.\n");
        break;
    }

    sb.append("Usage: java ");
    sb.append(SocketServer.class);
    sb.append(" [SOCKET_SERVER_PROPERTIES_FILEPATH]");

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
          SocketServer.this.destroy();
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
        logger.info("Destroy Socket Server");
        super.destroy();
        destroyed = true;
      }
    }
  }

  public void setupJmx() throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
    StringBuilder sb;
    String tmp;
    SocketServerJmx ssmbImpl;

    logger.info("Setting Up JMX Management for Socket Server");

    sb = new StringBuilder();
    sb.append(JMX_ROOT_NAME);
    sb.append(":port=");
    sb.append(socketInfo.getPort());

    tmp = sb.toString();

    ssmbImpl = new SocketServerJmx();
    ssmbImpl.setSocketServer(this);

    ssmb = ssmbImpl;

    jmxServer = ManagementFactory.getPlatformMBeanServer();
    jmxObjName = new ObjectName(tmp);

    jmxServer.registerMBean(ssmb, jmxObjName);
  }

  public static void main(String[] args) {
    int exitCd, checkArgStatus;
    SocketServer server = null;
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

        server = new SocketServer();

        server.loadSocketFrameworkProperties(filePath);

        server.init();

        server.setupJmx();

        server.startSocketHandling();

        server.enableShutdownHook();

        server.waitWhileIsHandlingSockets();

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
            server.destroy();
          }
        }
        catch (Exception e) {
          logger.error(e);
        }
      }
    }

    System.exit(exitCd);
  }

}
