/**
 * Created Aug 29, 2021
 */
package com.ilardi.experiments.dbms.engine;

import java.lang.reflect.Constructor;
import java.util.Properties;

import com.ilardi.experiments.dbms.AppDbException;
import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author robert.ilardi
 *
 */

public final class DbmsEngineFactoryFactory {

  private static final Logger logger = LogUtil.getInstance().getLogger(DbmsEngineFactoryFactory.class);

  private static final String PROP_DBMS_ENGINE_FACTORY_CLASSNAME = "AppDb.DbmsEngineFactory.Classname";

  private static DbmsEngineFactoryFactory abstractFactoryInstance = null;

  private DbmsEngineFactory engineFactory = null;

  private DbmsEngineFactoryFactory() {}

  public static synchronized DbmsEngineFactoryFactory getInstance() {
    if (abstractFactoryInstance == null) {
      abstractFactoryInstance = new DbmsEngineFactoryFactory();
    }

    return abstractFactoryInstance;
  }

  public synchronized DbmsEngineFactory getDbmsEngineFactory(Properties dbmsProps) throws AppDbException {
    if (engineFactory == null) {
      engineFactory = createDbmsEngineFactory(dbmsProps);
    }

    return engineFactory;
  }

  @SuppressWarnings("unchecked")
  private synchronized DbmsEngineFactory createDbmsEngineFactory(Properties dbmsProps) throws AppDbException {
    DbmsEngineFactory deFactory = null;
    String componentClassname = null;
    Class<? extends DbmsEngineFactory> clazz;
    Constructor<? extends DbmsEngineFactory> construct;

    try {
      componentClassname = dbmsProps.getProperty(PROP_DBMS_ENGINE_FACTORY_CLASSNAME);

      if (componentClassname == null) {
        throw new AppDbException("DBMS Engine Factory Classname is NULL!");
      }

      componentClassname = componentClassname.trim();

      if (componentClassname.length() == 0) {
        throw new AppDbException("DBMS Engine Factory Classname is Empty String!");
      }

      logger.info("Creating DBMS Engine Factory Instance: " + componentClassname);

      clazz = (Class<? extends DbmsEngineFactory>) Class.forName(componentClassname);

      construct = clazz.getConstructor();

      deFactory = construct.newInstance();

      return deFactory;
    }
    catch (Exception e) {
      throw new AppDbException("An error occurred while attempting to Create DBMS Engine Factory: " + componentClassname + " ; System Message: " + e.getMessage(), e);
    }
  }

}
