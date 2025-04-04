/**
 * Created Aug 29, 2021
 */
package com.ilardi.experiments.dbms.engine;

import java.lang.reflect.Constructor;

import com.ilardi.experiments.dbms.AppDbException;
import com.ilardi.experiments.dbms.mediator.DbmsMediator;
import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author robert.ilardi
 *
 */

public class DbmsComponentFactory {

  private static final Logger logger = LogUtil.getInstance().getLogger(DbmsComponentFactory.class);

  private static DbmsComponentFactory factoryInstance = null;

  private DbmsComponentFactory() {}

  public static synchronized DbmsComponentFactory getInstance() {
    if (factoryInstance == null) {
      factoryInstance = new DbmsComponentFactory();
    }

    return factoryInstance;
  }

  @SuppressWarnings("unchecked")
  public DbmsBaseComponent createDbmsComponent(String componentClassname, DbmsInfo info, DbmsMediator mediator) throws AppDbException {
    DbmsBaseComponent dbc = null;
    Class<? extends DbmsBaseComponent> clazz;
    Constructor<? extends DbmsBaseComponent> construct;

    try {
      if (componentClassname == null) {
        throw new AppDbException("DBMS Component Classname is NULL!");
      }

      componentClassname = componentClassname.trim();

      if (componentClassname.length() == 0) {
        throw new AppDbException("DBMS Component Classname is Empty String!");
      }

      logger.info("Creating DBMS Component Instance: " + componentClassname);

      clazz = (Class<? extends DbmsBaseComponent>) Class.forName(componentClassname);

      construct = clazz.getConstructor();

      dbc = construct.newInstance();

      dbc.setDbmsInfo(info);
      dbc.setDbmsMediator(mediator);

      return dbc;
    }
    catch (Exception e) {
      throw new AppDbException("An error occurred while attempting to Create DBMS Component: " + componentClassname + " ; System Message: " + e.getMessage(), e);
    }
  }

}
