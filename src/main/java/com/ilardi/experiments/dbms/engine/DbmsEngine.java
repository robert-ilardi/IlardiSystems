/**
 * Created Apr 1, 2021
 */
package com.ilardi.experiments.dbms.engine;

import java.util.Properties;

import com.ilardi.experiments.dbms.AppDbException;

/**
 * @author robert.ilardi
 *
 */

public interface DbmsEngine {

  public void startEngine() throws AppDbException;

  public void stopEngine() throws AppDbException;

  public void waitWhileEngineRunning() throws AppDbException;

  public void setAppDbProperties(Properties appDbProperties);

  public void createEngine() throws AppDbException;

}
