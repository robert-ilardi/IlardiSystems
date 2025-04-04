/**
 * Created Jan 24, 2021
 */
package com.ilardi.experiments.log;

/**
 * @author robert.ilardi
 *
 */

public interface Logger {

  public void init(String name);

  public void trace(Object message);

  public void debug(Object message);

  public void info(Object message);

  public void warn(Object message);

  public void error(Object message);

  public void error(Throwable t);

  public void fatal(Object message);

  public void fatal(Throwable t);

}
