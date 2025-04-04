/**
 * Created Aug 29, 2021
 */
package com.ilardi.experiments.dbms.engine;

import java.util.Properties;

import com.ilardi.experiments.dbms.AppDbException;
import com.ilardi.experiments.dbms.mediator.DbmsMediator;

/**
 * @author robert.ilardi
 *
 */

public abstract class BaseDbmsEngine implements DbmsEngine {

  protected Properties appDbProperties;

  public BaseDbmsEngine() {}

  @Override
  public void setAppDbProperties(Properties appDbProperties) {
    this.appDbProperties = appDbProperties;
  }

  protected DbmsBaseComponent createDbmsComponent(String componentClassname, DbmsInfo info, DbmsMediator mediator) throws AppDbException {
    DbmsComponentFactory componentFactory;
    DbmsBaseComponent component;

    if (componentClassname == null) {
      throw new AppDbException("DBMS Component Classname is NULL!");
    }

    componentClassname = componentClassname.trim();

    if (componentClassname.length() == 0) {
      throw new AppDbException("DBMS Component Classname is Empty String!");
    }

    componentFactory = DbmsComponentFactory.getInstance();

    component = componentFactory.createDbmsComponent(componentClassname, info, mediator);

    return component;
  }

}
