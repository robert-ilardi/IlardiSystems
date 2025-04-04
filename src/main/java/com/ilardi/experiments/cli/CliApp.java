/**
 * Created Apr 24, 2023
 */
package com.ilardi.experiments.cli;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Properties;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author robert.ilardi
 *
 */

public class CliApp {

  private static final Logger logger = LogUtil.getInstance().getLogger(CliApp.class);

  private static final String JMX_ROOT_NAME = "IlardiSystems.Framework.CliApp";

  private static CliApp cliAppInstance;

  private final Object cliAppLock;

  private volatile boolean halted = true;

  private String cliAppPropertiesFilepath;

  private Properties cliAppProperties;

  private MBeanServer jmxServer;
  private ObjectName jmxObjName;
  private CliAppJmxMBean cliAppMb;

  private String PROP_CLIAPP_APP_NAME = "IlardiSystems.Framework.CliApp.AppName";

  private String cliAppName;

  private CliApp() {
    cliAppLock = new Object();
    halted = true;
  }

  public static synchronized CliApp getInstance() {
    if (cliAppInstance == null) {
      cliAppInstance = new CliApp();
    }

    return cliAppInstance;
  }

  public void startup() throws CliAppException {
    synchronized (cliAppLock) {
      logger.info("Starting Cli App Framework...");

      loadCliAppProperties(this.cliAppPropertiesFilepath);

      setupJmx();

      halted = false;

      cliAppLock.notifyAll();
    }
  }

  public void loadCliAppProperties(String cliAppPropertiesFilepath) throws CliAppException {
    FileInputStream fis = null;

    try {
      logger.info("Loading CliApp Properties: " + cliAppPropertiesFilepath);

      this.cliAppPropertiesFilepath = cliAppPropertiesFilepath;

      fis = new FileInputStream(this.cliAppPropertiesFilepath);

      loadCliAppProperties(fis);
    }
    catch (Exception e) {
      throw new CliAppException("An error occurred while attempting to Load CliApp Properties. System Message: " + e.getMessage(), e);
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

  public void loadCliAppProperties(InputStream ins) throws IOException {
    Properties props;

    logger.info("Loading CliApp Properties from Input Stream");

    props = new Properties();

    props.load(ins);

    setCliAppProperties(props);
  }

  public void setCliAppProperties(Properties cliAppProperties) {
    this.cliAppProperties = cliAppProperties;
  }

  public String getCliAppProperty(String propName) {
    String propVal;

    propVal = cliAppProperties.getProperty(propName);

    return propVal;
  }

  public void shutdown() throws CliAppException {
    synchronized (cliAppLock) {
      if (!halted) {
        logger.info("Shutdown CliApp");

        halted = true;
      }
    }
  }

  public void waitWhileRunning() throws CliAppException {
    synchronized (cliAppLock) {
      try {
        while (!halted) {
          cliAppLock.wait();
        }
      }
      catch (Exception e) {
        throw new CliAppException("An error occurred waiting while CliApp was Running! System Message: " + e.getMessage(), e);
      }
    }
  }

  private void setupJmx() throws CliAppException {
    StringBuilder sb;
    String tmp;
    CliAppJmx cliAppMbImpl;

    try {
      logger.info("Setting Up JMX Management for CliApp");

      sb = new StringBuilder();
      sb.append(JMX_ROOT_NAME);
      sb.append(":type=Server,name=");
      sb.append(cliAppName);

      tmp = sb.toString();

      cliAppMbImpl = new CliAppJmx();
      this.cliAppMb = cliAppMbImpl;

      jmxServer = ManagementFactory.getPlatformMBeanServer();
      jmxObjName = new ObjectName(tmp);

      logger.info("Registering JMX MBean: " + tmp);

      jmxServer.registerMBean(cliAppMb, jmxObjName);
    } // End try block
    catch (Exception e) {
      throw new CliAppException("An error occurred while attempting to Setup JMX. System Message: " + e.getMessage(), e);
    }
  }

  public static synchronized String getCheckArgMessage(int checkArgStatus) {
    StringBuilder sb = new StringBuilder();

    switch (checkArgStatus) {
      case 1:
        sb.append("Error: Command Line Argument [CLI_APP_PROPERTIES_FILEPATH] NOT Set. Please check command line parameters and rerun program.\n");
        break;
      case 2:
        sb.append("Error: Command Line Argument [CLI_APP_PROPERTIES_FILEPATH] CANNOT Be NULL. Please check command line parameters and rerun program.\n");
        break;
      case 3:
        sb.append("Error: Command Line Argument [CLI_APP_PROPERTIES_FILEPATH] CANNOT be an Empty String (\"\"). Please check command line parameters and rerun program.\n");
        break;
    }

    sb.append("Usage: java ");
    sb.append(CliApp.class);
    sb.append(" [CLI_APP_PROPERTIES_FILEPATH]");

    return sb.toString();
  }

  public static synchronized int checkCmdLineArgs(String[] startupArgs, Properties startupProps) {
    int status;

    if (startupArgs.length == 0) {
      status = 1;
    }
    else if (startupArgs[0] == null) {
      status = 2;
    }
    else if (startupArgs[0].trim().length() == 0) {
      status = 3;
    }
    else {
      status = 0;
    }

    return status;
  }

  public void parseStartupCmdLine(String[] startupArgs, Properties startupProps) {
    synchronized (cliAppLock) {
      cliAppPropertiesFilepath = startupArgs[0];
      cliAppPropertiesFilepath = cliAppPropertiesFilepath.trim();
    }
  }

  public static void main(String[] args) {
    int exitCd = -1, checkArgStatus;
    String errMesg, tmp;
    CliApp cli = null;

    checkArgStatus = checkCmdLineArgs(args, System.getProperties());

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

        cli = CliApp.getInstance();

        cli.parseStartupCmdLine(args, System.getProperties());

        cli.waitWhileRunning();

        exitCd = 0;
      }
      catch (Exception e) {
        exitCd = 1;
        e.printStackTrace();
        logger.fatal(e);
      }
      finally {
        try {
          if (cli != null) {
            cli.shutdown();
          }
        }
        catch (Exception e) {
          e.printStackTrace();
          logger.error(e);
        }
      }
    }

    logger.debug("Exiting with Status Code = " + exitCd);

    System.exit(exitCd);
  }

}
