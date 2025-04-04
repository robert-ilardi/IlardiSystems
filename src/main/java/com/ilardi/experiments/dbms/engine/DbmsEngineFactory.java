/**
 * Created Aug 29, 2021
 */
package com.ilardi.experiments.dbms.engine;

import java.util.Properties;

import com.ilardi.experiments.dbms.AppDbException;

/**
 * @author robert.ilardi
 *
 */

public interface DbmsEngineFactory {

  public DbmsEngine createDbmsEngine(Properties appDbProps) throws AppDbException;

}
