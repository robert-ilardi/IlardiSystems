/**
 * Created Jul 6, 2023
 */
package io.ilardi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author robert.ilardi
 *
 */

public final class ApplicationContext {

  private static final Logger logger = LogManager.getLogger(ApplicationContext.class);

  public static final String SYS_PROP_DEFAULT_APP_PROPS_PATH = "ilardi.app.AppPropsPath";

  private static final String SYS_PROP_PREFIX_APP_PROPS_CLASSPATH = "CLASSPATH://";

  private static final String DEFAULT_APP_PROPS_FILENAME = "ilardi-app.properties";
  private static final String DEFAULT_APP_PROPS_CLASSPATH = "CLASSPATH://" + DEFAULT_APP_PROPS_FILENAME;

  private static final String PROP_APP_MODE = "ilardi.AppContext.appMode";

  private static final String PROP_TEMP_DIR = "ilardi.AppContext.tempDir";

  private static ApplicationContext instance = null;

  private final Object instanceLock;

  private boolean destroyed;

  private HashMap<Object, Object> appSession;

  private boolean appRunning;

  private String usrHome;

  private String appPropsPath;

  private Properties appProps;

  private ApplicationMode appMode;

  private String tempDir;

  private ApplicationContext() {
    instanceLock = new Object();

    destroyed = false;

    appRunning = false;

    appSession = new HashMap<Object, Object>();
  }

  public final synchronized static ApplicationContext getInstance() throws IlardiException {
    if (instance == null) {
      instance = new ApplicationContext();
      instance.init();
    }

    return instance;
  }

  private void init() throws IlardiException {
    synchronized (instanceLock) {
      logger.debug("Application Context Initializing...");

      locateUserHomeDirectory();

      locateAppPropertiesPath();

      loadAppProperties();

      loadAppMode();

      loadTempDir();

      instanceLock.notifyAll();
    }
  }

  private void loadAppMode() throws IlardiException {
    String tmp;

    tmp = getAppProperty(PROP_APP_MODE);
    if (tmp != null && !tmp.isBlank()) {
      tmp = tmp.trim();
      appMode = ApplicationMode.valueOf(tmp);
    }
    else {
      appMode = ApplicationMode.DEFAULT;
    }
  }

  private void loadTempDir() throws IlardiException {
    String tmp;

    tmp = getAppProperty(PROP_TEMP_DIR);
    if (tmp != null && !tmp.isBlank()) {
      tempDir = tmp.trim();
    }
  }

  public ApplicationMode getApplicationMode() {
    return appMode;
  }

  public String getTempDir() {
    return tempDir;
  }

  public void destroy() throws IlardiException {
    synchronized (instanceLock) {
      if (destroyed) {
        return;
      }

      usrHome = null;

      appSession.clear();
      appSession = null;

      destroyed = true;

      instanceLock.notifyAll();
    }
  }

  public String getUsrHome() {
    return usrHome;
  }

  public void setUsrHome(String usrHome) {
    this.usrHome = usrHome;
  }

  public String getAppPropsPath() {
    return appPropsPath;
  }

  public Object appSessionPut(Object key, Object value) {
    synchronized (instanceLock) {
      return appSession.put(key, value);
    }
  }

  public Object appSessionGet(Object key) {
    synchronized (instanceLock) {
      return appSession.get(key);
    }
  }

  public Object appSessionRemove(Object key) {
    synchronized (instanceLock) {
      return appSession.remove(key);
    }
  }

  public void appSessionClear() {
    synchronized (instanceLock) {
      appSession.clear();
    }
  }

  public int appSessionSize() {
    synchronized (instanceLock) {
      return appSession.size();
    }
  }

  public boolean appSessionContainsKey(Object key) {
    synchronized (instanceLock) {
      return appSession.containsKey(key);
    }
  }

  public boolean appSessionIsEmpty() {
    synchronized (instanceLock) {
      return appSession.isEmpty();
    }
  }

  public boolean isAppRunning() {
    synchronized (instanceLock) {
      return appRunning;
    }
  }

  public void setAppRunning(boolean appRunning) {
    synchronized (instanceLock) {
      this.appRunning = appRunning;
      instanceLock.notifyAll();
    }
  }

  private void locateUserHomeDirectory() {
    StringBuilder sb = null;

    synchronized (instanceLock) {
      if (usrHome == null) {
        logger.debug("Locating User Home Directory");

        usrHome = IlardiUtils.getHomeDirectory();

        if (usrHome != null) {
          if (!usrHome.endsWith("\\") && !usrHome.endsWith("/")) {
            sb = new StringBuilder();

            sb.append(usrHome);
            sb.append("/");

            usrHome = sb.toString();
            sb = null;
          }
        }

        logger.debug("Setting User Home Directory to: " + usrHome);
      }
    }
  }

  public String getAppProperty(String propName) throws IlardiException {
    synchronized (instanceLock) {
      try {
        return appProps.getProperty(propName);
      } // End try block
      catch (Exception e) {
        throw new IlardiException("An error occurred while attempting to Get App Property. System Message: " + e.getMessage(), e);
      }
    }
  }

  public void setAppProperty(String propName, String propValue) throws IlardiException {
    synchronized (instanceLock) {
      try {
        appProps.setProperty(propName, propValue);
      } // End try block
      catch (Exception e) {
        throw new IlardiException("An error occurred while attempting to Set App Property. System Message: " + e.getMessage(), e);
      }
    }
  }

  public void removeAppProperty(String propName) throws IlardiException {
    synchronized (instanceLock) {
      try {
        appProps.remove(propName);
      } // End try block
      catch (Exception e) {
        throw new IlardiException("An error occurred while attempting to Remove App Property. System Message: " + e.getMessage(), e);
      }
    }
  }

  public void clearAppProperty() throws IlardiException {
    synchronized (instanceLock) {
      try {
        appProps.clear();
      } // End try block
      catch (Exception e) {
        throw new IlardiException("An error occurred while attempting to Clear App Properties. System Message: " + e.getMessage(), e);
      }
    }
  }

  private void locateAppPropertiesPath() {
    StringBuilder sb;
    String sysProp;

    synchronized (instanceLock) {
      if (appPropsPath == null) {
        logger.debug("Locating App Properties Path...");

        sysProp = System.getProperty(SYS_PROP_DEFAULT_APP_PROPS_PATH);

        if (sysProp == null || sysProp.isBlank()) {
          sb = new StringBuilder();
          sb.append(usrHome);

          sb.append(DEFAULT_APP_PROPS_FILENAME);

          appPropsPath = sb.toString();
          sb = null;

          if (IlardiUtils.fileExists(appPropsPath)) {
            logger.debug("Using User Home Directory");
          }
          else {
            appPropsPath = DEFAULT_APP_PROPS_CLASSPATH;
            logger.debug("Using Default App Properties Classpath");
          }
        }
        else {
          appPropsPath = sysProp.trim();
          sysProp = null;

          logger.debug("Using System Property Override");
        }

        logger.debug("Setting App Properties Path to: " + appPropsPath);
      } // End null appPropsPath Check
    } // End Sync Block
  }

  private void loadAppProperties() throws IlardiException {
    synchronized (instanceLock) {
      try {
        logger.debug("Loading App Properties File into Application Context");

        appProps = loadPropertiesFile(appPropsPath);

        logger.debug("Successfully Loaded " + appProps.size() + " Properties from App Properties in: " + appPropsPath);
      } // End try block
      catch (Exception e) {
        throw new IlardiException("An error occurred while attempting to Load App Properties. System Message: " + e.getMessage(), e);
      }
    }
  }

  private Properties loadPropertiesFile(String propsPath) throws IOException {
    Properties props = null;
    InputStream ins = null;

    try {
      logger.debug("Loading Properties File: " + propsPath);

      if (propsPath.toUpperCase().startsWith(SYS_PROP_PREFIX_APP_PROPS_CLASSPATH)) {
        // Read from Classpath
        propsPath = propsPath.substring(SYS_PROP_PREFIX_APP_PROPS_CLASSPATH.length());
        ins = ApplicationContext.class.getClassLoader().getResourceAsStream(propsPath);
      }
      else {
        // Read from File System
        ins = new FileInputStream(propsPath);
      }

      props = new Properties();
      props.load(ins);

      return props;
    } // End try block
    finally {
      try {
        if (ins != null) {
          ins.close();
          ins = null;
        }
      }
      catch (Exception e) {
        logger.throwing(e);
      }
    }
  }

  public void saveAppProperties(String optionalComment) throws IlardiException {

    synchronized (instanceLock) {
      logger.debug("Saving App Properties File from Application Context");

      try {
        saveProperties(appProps, appPropsPath, optionalComment);
      } // End try block
      catch (Exception e) {
        throw new IlardiException("An error occurred while attempting to Save App Properties. System Message: " + e.getMessage(), e);
      }
    }
  }

  public void saveProperties(Properties props, String filePath, String optionalComment) throws IOException {
    FileOutputStream fos = null;

    synchronized (instanceLock) {
      logger.debug("Loading Properties File: " + filePath);

      try {
        fos = new FileOutputStream(filePath);
        props.store(fos, optionalComment);
      }
      finally {
        try {
          if (fos != null) {
            fos.close();
            fos = null;
          }
        }
        catch (Exception e) {
          logger.throwing(e);
        }
      }
    }

  }

  public List<String> getAppPropertyNames() throws IlardiException {
    Set<Object> keySet;
    Iterator<Object> iter;
    String propName;
    ArrayList<String> propNames;

    synchronized (instanceLock) {
      try {
        keySet = appProps.keySet();
        iter = keySet.iterator();

        propNames = new ArrayList<String>();

        while (iter.hasNext()) {
          propName = (String) iter.next();

          if (propName != null) {
            propName = propName.trim();
          }

          propNames.add(propName);
        }

        iter = null;
        keySet = null;
        propName = null;

        return propNames;
      } // End try block
      catch (Exception e) {
        throw new IlardiException("An error occurred while attempting to Build List of App Property Names. System Message: " + e.getMessage(), e);
      }
    }
  }

  public Properties getAppPropertiesByPropPrefix(String propsPrefix) {
    Properties subProps;

    subProps = IlardiStringUtils.getPropertiesUsingPropNamePrefix(appProps, propsPrefix);

    return subProps;
  }

}
