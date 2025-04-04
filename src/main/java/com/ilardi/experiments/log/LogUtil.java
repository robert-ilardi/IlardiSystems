/**
 * Created Jan 24, 2021
 */
package com.ilardi.experiments.log;

import java.util.HashMap;

/**
 * @author robert.ilardi
 *
 */

public class LogUtil {

  private static final String SYS_PROP_LOGGER_PROVIDER = "com.ilardi.log.provider";

  private static final String DEFAULT_LOGGER_PROVIDER = JavaLogger.class.getName();

  private HashMap<String, Logger> loggerMap;

  private static LogUtil instance;

  private LogUtil() {
    loggerMap = new HashMap<String, Logger>();
  }

  public static synchronized LogUtil getInstance() {
    if (instance == null) {
      instance = new LogUtil();
    }

    return instance;
  }

  @SuppressWarnings("unchecked")
  public synchronized Logger getLogger(String name) {
    Logger logger = null;
    String provider;
    Class<Logger> lc;

    try {
      logger = loggerMap.get(name);

      if (logger == null) {
        provider = System.getProperty(SYS_PROP_LOGGER_PROVIDER);

        if (provider == null || provider.trim().length() == 0) {
          provider = DEFAULT_LOGGER_PROVIDER;
        }

        provider = provider.trim();

        lc = (Class<Logger>) Class.forName(provider);

        logger = lc.newInstance();

        logger.init(name);

        loggerMap.put(name, logger);
      }
    } // End try block
    catch (Exception e) {
      e.printStackTrace();
    }

    return logger;
  }

  public synchronized Logger getLogger(Class<?> clazz) {
    return getLogger(clazz.getName());
  }

}
