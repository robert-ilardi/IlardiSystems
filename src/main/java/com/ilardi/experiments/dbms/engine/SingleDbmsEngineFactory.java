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

public class SingleDbmsEngineFactory implements DbmsEngineFactory {

  private static final Logger logger = LogUtil.getInstance().getLogger(DbmsEngineFactoryFactory.class);

  private static String PROP_DBMS_ENGINE_CLASSNAME = "AppDb.SingleDbmsEngineFactory.DbmsEngine.Classname";

  public SingleDbmsEngineFactory() {}

  @SuppressWarnings("unchecked")
  public DbmsEngine createDbmsEngine(Properties appDbProps) throws AppDbException {
    DbmsEngine engine = null;
    String componentClassname = null;
    Class<? extends DbmsEngine> clazz;
    Constructor<? extends DbmsEngine> construct;

    try {
      componentClassname = appDbProps.getProperty(PROP_DBMS_ENGINE_CLASSNAME);

      if (componentClassname == null) {
        throw new AppDbException("DBMS Engine Classname is NULL!");
      }

      componentClassname = componentClassname.trim();

      if (componentClassname.length() == 0) {
        throw new AppDbException("DBMS Engine Classname is Empty String!");
      }

      logger.info("Creating DBMS Engine Instance: " + componentClassname);

      clazz = (Class<? extends DbmsEngine>) Class.forName(componentClassname);

      construct = clazz.getConstructor();

      engine = construct.newInstance();

      engine.setAppDbProperties(appDbProps);

      return engine;
    }
    catch (Exception e) {
      throw new AppDbException("An error occurred while attempting to Create DBMS Engine: " + componentClassname + " ; System Message: " + e.getMessage(), e);
    }
  }

}
