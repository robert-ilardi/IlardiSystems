/**
 * Created Jan 24, 2021
 */
package com.ilardi.experiments.log;

import java.util.logging.Level;

/**
 * @author robert.ilardi
 *
 */

public class JavaLogger extends BaseLogger {

  private java.util.logging.Logger logger;

  @Override
  public void init(String name) {
    logger = java.util.logging.Logger.getLogger(name);
  }

  @Override
  public void trace(Object message) {
    logger.logp(Level.FINEST, getCallingClassName(), getCallingMethodName(), (message != null ? message.toString() : null));
  }

  @Override
  public void debug(Object message) {
    logger.logp(Level.FINE, getCallingClassName(), getCallingMethodName(), (message != null ? message.toString() : null));
  }

  @Override
  public void info(Object message) {
    logger.logp(Level.INFO, getCallingClassName(), getCallingMethodName(), (message != null ? message.toString() : null));
  }

  @Override
  public void warn(Object message) {
    logger.logp(Level.WARNING, getCallingClassName(), getCallingMethodName(), (message != null ? message.toString() : null));
  }

  @Override
  public void error(Object message) {
    logger.logp(Level.WARNING, getCallingClassName(), getCallingMethodName(), (message != null ? message.toString() : null));
  }

  @Override
  public void fatal(Object message) {
    logger.logp(Level.SEVERE, getCallingClassName(), getCallingMethodName(), (message != null ? message.toString() : null));
  }

}
