/**
 * Created Mar 28, 2021
 */
package com.ilardi.experiments.dbms;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Properties;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.ilardi.experiments.dbms.kernel.AppDbKernel;
import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author robert.ilardi
 *
 */

public class AppDb {

  private static final Logger logger = LogUtil.getInstance().getLogger(AppDb.class);

  private static final String JMX_ROOT_NAME = "AppDb.Dbms";

  private final Object appDbLock;

  private volatile boolean halted = false;

  private String appDbPropertiesFile;

  private Properties appDbProperties;

  private AppDbKernel kernel;

  private MBeanServer jmxServer;
  private ObjectName jmxObjName;
  private AppDbJmxMBean appDbMb;

  public AppDb() {
    appDbLock = new Object();
  }

  public void enableShutdownHook() {
    logger.info("Enabling Shutdown Hook");

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        try {
          logger.info("Running Shutdown Hook");
          AppDb.this.shutdown();
        }
        catch (Exception e) {
          logger.error(e);
        }
      }
    });
  }

  public void loadProperties(String appDbPropertiesFile) throws AppDbException {
    FileInputStream fis = null;

    try {
      logger.info("Loading AppDb Properties: " + appDbPropertiesFile);

      this.appDbPropertiesFile = appDbPropertiesFile;

      fis = new FileInputStream(this.appDbPropertiesFile);

      loadProperties(fis);
    }
    catch (Exception e) {
      throw new AppDbException("An error occurred while attempting to Load AppDb Properties. System Message: " + e.getMessage(), e);
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

  private void loadProperties(FileInputStream fis) throws IOException {
    Properties props = new Properties();

    props.load(fis);

    setProperties(props);
  }

  public void boot() throws AppDbException {
    synchronized (appDbLock) {
      kernel = AppDbKernel.getKernelInstance();

      kernel.setProperties(appDbProperties);

      kernel.boot();
    }
  }

  public void setProperties(Properties appDbProperties) {
    this.appDbProperties = appDbProperties;
  }

  public void shutdown() throws AppDbException {
    synchronized (appDbLock) {
      if (!halted) {
        logger.info("Shutdown AppDb");

        if (kernel != null) {
          kernel.shutdown();
          kernel = null;
        }

        halted = true;
      }
    }
  }

  public void waitWhileRunning() throws AppDbException {
    if (kernel != null) {
      kernel.waitWhileRunning();
    }
  }

  public void setupJmx() throws AppDbException {
    StringBuilder sb;
    String tmp, instanceName;
    AppDbJmx appDbMbImpl;

    try {
      logger.info("Setting Up JMX Management for AppDb");

      instanceName = kernel.getKernelInstanceName();

      sb = new StringBuilder();
      sb.append(JMX_ROOT_NAME);
      sb.append(":appDbName=");
      sb.append(instanceName);

      tmp = sb.toString();

      appDbMbImpl = new AppDbJmx();
      appDbMbImpl.setAppDb(this);

      appDbMb = appDbMbImpl;

      jmxServer = ManagementFactory.getPlatformMBeanServer();
      jmxObjName = new ObjectName(tmp);

      jmxServer.registerMBean(appDbMb, jmxObjName);
    } // End try block
    catch (Exception e) {
      throw new AppDbException("An error occurred while attempting to Setup JMX. System Message: " + e.getMessage(), e);
    }
  }

  private static String getCheckArgMessage(int checkArgStatus) {
    StringBuilder sb = new StringBuilder();

    switch (checkArgStatus) {
      case 1:
        sb.append("Error: Command Line Argument [APP_DB_PROPERTIES_FILEPATH] NOT Set. Please check command line parameters and rerun program.\n");
        break;
      case 2:
        sb.append("Error: Command Line Argument [APP_DB_PROPERTIES_FILEPATH] CANNOT Be NULL. Please check command line parameters and rerun program.\n");
        break;
      case 3:
        sb.append("Error: Command Line Argument [APP_DB_PROPERTIES_FILEPATH] CANNOT be an Empty String (\"\"). Please check command line parameters and rerun program.\n");
        break;
    }

    sb.append("Usage: java ");
    sb.append(AppDb.class);
    sb.append(" [APP_DB_PROPERTIES_FILEPATH]");

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
    AppDb dbms = null;
    String filePath, errMesg, tmp;

    checkArgStatus = checkCmdLineArgs(args);

    if (checkArgStatus != 0) {
      exitCd = 1;
      errMesg = getCheckArgMessage(checkArgStatus);
      System.err.println(errMesg);
      logger.error(errMesg);
    }
    else {
      try {
        tmp = Version.getVersionInfo();

        System.out.println(tmp);
        logger.info(tmp);

        filePath = args[0];
        filePath = filePath.trim();

        dbms = new AppDb();

        dbms.loadProperties(filePath);

        dbms.boot();

        dbms.setupJmx();

        dbms.enableShutdownHook();

        dbms.waitWhileRunning();

        exitCd = 0;
      }
      catch (Exception e) {
        exitCd = 1;
        e.printStackTrace();
        logger.fatal(e);
      }
      finally {
        try {
          if (dbms != null) {
            dbms.shutdown();
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
