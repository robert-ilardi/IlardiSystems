/**
 * Created Aug 29, 2021
 */
package com.ilardi.experiments.dbms.engine;

import java.util.Properties;

import com.ilardi.experiments.dbms.AppDbException;
import com.ilardi.experiments.dbms.endpoint.DbmsEndpointManager;
import com.ilardi.experiments.dbms.mediator.DbmsMediator;
import com.ilardi.experiments.dbms.query.DbmsQueryManager;
import com.ilardi.experiments.dbms.security.DbmsSecurityManager;
import com.ilardi.experiments.dbms.security.authentication.DbmsAuthenticationManager;
import com.ilardi.experiments.dbms.security.authorization.DbmsAuthorizationManager;
import com.ilardi.experiments.dbms.session.DbmsSessionManager;
import com.ilardi.experiments.dbms.storage.DbmsStorageManager;
import com.ilardi.experiments.dbms.transport.DbmsTransportManager;
import com.ilardi.experiments.dbms.transport.DbmsTransportType;
import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author robert.ilardi
 *
 */

public class SingleDbmsEngine extends BaseDbmsEngine {

  private static final Logger logger = LogUtil.getInstance().getLogger(SingleDbmsEngine.class);

  private static final String PROP_DBMS_NAME = "AppDb.DbmsName";

  private static final String PROP_DBMS_SYSDB_JEFS_FILEPATH = "AppDb.DbmsSystemDatabaseJefsFilePath";

  private static final String PROP_DBMS_TYPE = "AppDb.DbmsType";
  private static final String PROP_DBMS_TRANSPORT_TYPE = "AppDb.DbmsTransportType";

  private static final String PROP_DBMS_AUTHENTICATION_CLASSNAME = "AppDb.DbmsAuthenticationManagerClassname";
  private static final String PROP_DBMS_AUTHORIZATION_CLASSNAME = "AppDb.DbmsAuthorizationManagerClassname";
  private static final String PROP_DBMS_ENDPOINT_CLASSNAME = "AppDb.DbmsEndpointManagerClassname";
  private static final String PROP_DBMS_MEDIATOR_CLASSNAME = "AppDb.DbmsMediatorClassname";
  private static final String PROP_DBMS_QUERY_CLASSNAME = "AppDb.DbmsQueryManagerClassname";
  private static final String PROP_DBMS_SECURITY_CLASSNAME = "AppDb.DbmsSecurityManagerClassname";
  private static final String PROP_DBMS_SESSION_CLASSNAME = "AppDb.DbmsSessionManagerClassname";
  private static final String PROP_DBMS_STORAGE_CLASSNAME = "AppDb.DbmsStorageManagerClassname";
  private static final String PROP_DBMS_TRANSPORT_CLASSNAME = "AppDb.DbmsTransportManagerClassname";

  private final Object engineLock;

  private volatile boolean engineRunning = false;

  private DbmsInfo dbmsInfo;

  private DbmsMediator dbmsMediator;

  public SingleDbmsEngine() {
    super();

    engineLock = new Object();
  }

  @Override
  public void startEngine() throws AppDbException {
    synchronized (engineLock) {
      logger.info("Engine Start");

      if (dbmsMediator != null) {
        dbmsMediator.initDbmsComponent();
      }
      else {
        throw new AppDbException("DBMS Mediator is NULL!");
      }

      engineRunning = true;

      engineLock.notifyAll();
    }
  }

  @Override
  public void stopEngine() throws AppDbException {
    synchronized (engineLock) {
      logger.info("Engine Stop");

      if (dbmsMediator != null) {
        dbmsMediator.shutdownDbmsComponent();
      }

      engineRunning = false;

      engineLock.notifyAll();
    }
  }

  @Override
  public void waitWhileEngineRunning() throws AppDbException {
    synchronized (engineLock) {
      logger.info("Entering Waiting State on Enging Running Status");

      try {
        while (engineRunning) {
          engineLock.wait();
        }
      }
      catch (Exception e) {
        throw new AppDbException("Error while Waiting on Engine Running. System Message: " + e.getMessage(), e);
      }

      logger.info("Exiting Waiting State on Enging Running Status");
    }
  }

  @Override
  public void createEngine() throws AppDbException {
    synchronized (engineLock) {
      logger.info("Engine Creation");

      dbmsInfo = createDbmsInfo(appDbProperties);

      dbmsMediator = createEngineComponents(dbmsInfo);

      logger.info("Engine Created using DBMS Info = " + dbmsInfo);

      engineLock.notifyAll();
    }
  }

  private DbmsInfo createDbmsInfo(Properties appDbProps) throws AppDbException {
    DbmsInfo info;
    String tmp;
    DbmsType dbmsType;
    DbmsTransportType transportType;

    info = new DbmsInfo();
    info.setDbmsProperties(appDbProps);

    tmp = appDbProps.getProperty(PROP_DBMS_NAME);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        info.setDbmsName(tmp);
      }
      else {
        throw new AppDbException("DBMS Name NOT Set! Please Check AppDb Properties.");
      }
    }
    else {
      throw new AppDbException("DBMS Name NOT Set! Please Check AppDb Properties.");
    }

    tmp = appDbProps.getProperty(PROP_DBMS_SYSDB_JEFS_FILEPATH);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        info.setSystemDatabaseJefsFilePath(tmp);
      }
      else {
        throw new AppDbException("System Database JEFS File Path NOT Set! Please Check AppDb Properties.");
      }
    }
    else {
      throw new AppDbException("System Database JEFS File Path NOT Set! Please Check AppDb Properties.");
    }

    tmp = appDbProps.getProperty(PROP_DBMS_TYPE);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        dbmsType = DbmsType.valueOf(tmp);
        info.setDbmsType(dbmsType);
      }
      else {
        throw new AppDbException("DBMS Type NOT Set! Please Check AppDb Properties.");
      }
    }
    else {
      throw new AppDbException("DBMS Type NOT Set! Please Check AppDb Properties.");
    }

    tmp = appDbProps.getProperty(PROP_DBMS_TRANSPORT_TYPE);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        transportType = DbmsTransportType.valueOf(tmp);
        info.setTransportType(transportType);
      }
      else {
        throw new AppDbException("Transport Type NOT Set! Please Check AppDb Properties.");
      }
    }
    else {
      throw new AppDbException("Transport Type NOT Set! Please Check AppDb Properties.");
    }

    tmp = appDbProps.getProperty(PROP_DBMS_AUTHENTICATION_CLASSNAME);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        info.setAuthenticationManagerClassname(tmp);
      }
      else {
        throw new AppDbException("Authentication Manager Classname NOT Set! Please Check AppDb Properties.");
      }
    }
    else {
      throw new AppDbException("Authentication Manager Classname NOT Set! Please Check AppDb Properties.");
    }

    tmp = appDbProps.getProperty(PROP_DBMS_AUTHORIZATION_CLASSNAME);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        info.setAuthorizationManagerClassname(tmp);
      }
      else {
        throw new AppDbException("Authorization Manager Classname NOT Set! Please Check AppDb Properties.");
      }
    }
    else {
      throw new AppDbException("Authorization Manager Classname NOT Set! Please Check AppDb Properties.");
    }

    tmp = appDbProps.getProperty(PROP_DBMS_TRANSPORT_CLASSNAME);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        info.setTransportManagerClassname(tmp);
      }
      else {
        throw new AppDbException("Transport Manager Classname NOT Set! Please Check AppDb Properties.");
      }
    }
    else {
      throw new AppDbException("Transport Manager Classname NOT Set! Please Check AppDb Properties.");
    }

    tmp = appDbProps.getProperty(PROP_DBMS_STORAGE_CLASSNAME);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        info.setStorageManagerClassname(tmp);
      }
      else {
        throw new AppDbException("Storage Manager Classname NOT Set! Please Check AppDb Properties.");
      }
    }
    else {
      throw new AppDbException("Storage Manager Classname NOT Set! Please Check AppDb Properties.");
    }

    tmp = appDbProps.getProperty(PROP_DBMS_SESSION_CLASSNAME);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        info.setSessionManagerClassname(tmp);
      }
      else {
        throw new AppDbException("Session Manager Classname NOT Set! Please Check AppDb Properties.");
      }
    }
    else {
      throw new AppDbException("Session Manager Classname NOT Set! Please Check AppDb Properties.");
    }

    tmp = appDbProps.getProperty(PROP_DBMS_QUERY_CLASSNAME);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        info.setQueryManagerClassname(tmp);
      }
      else {
        throw new AppDbException("Query Manager Classname NOT Set! Please Check AppDb Properties.");
      }
    }
    else {
      throw new AppDbException("Query Manager Classname NOT Set! Please Check AppDb Properties.");
    }

    tmp = appDbProps.getProperty(PROP_DBMS_SECURITY_CLASSNAME);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        info.setSecurityManagerClassname(tmp);
      }
      else {
        throw new AppDbException("Security Manager Classname NOT Set! Please Check AppDb Properties.");
      }
    }
    else {
      throw new AppDbException("Security Manager Classname NOT Set! Please Check AppDb Properties.");
    }

    tmp = appDbProps.getProperty(PROP_DBMS_ENDPOINT_CLASSNAME);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        info.setEndpointManagerClassname(tmp);
      }
      else {
        throw new AppDbException("Endpoint Manager Classname NOT Set! Please Check AppDb Properties.");
      }
    }
    else {
      throw new AppDbException("Endpoint Manager Classname NOT Set! Please Check AppDb Properties.");
    }

    tmp = appDbProps.getProperty(PROP_DBMS_MEDIATOR_CLASSNAME);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        info.setMediatorClassname(tmp);
      }
      else {
        throw new AppDbException("Mediator Classname NOT Set! Please Check AppDb Properties.");
      }
    }
    else {
      throw new AppDbException("Mediator Classname NOT Set! Please Check AppDb Properties.");
    }

    return info;
  }

  private DbmsMediator createEngineComponents(DbmsInfo info) throws AppDbException {
    synchronized (engineLock) {
      DbmsMediator mediator;
      DbmsSecurityManager securityMan;
      DbmsStorageManager storageMan;
      DbmsSessionManager sessionMan;
      DbmsQueryManager queryMan;
      DbmsTransportManager transportMan;
      DbmsEndpointManager endpointMan;

      logger.info("Engine Component Creation");

      mediator = createMediator(info); // Must be Created First

      securityMan = createSecurityManager(info, mediator);
      mediator.setSecurityMan(securityMan);

      storageMan = createStorageManager(info, mediator);
      mediator.setStorageMan(storageMan);

      sessionMan = createSessionManager(info, mediator);
      mediator.setSessionMan(sessionMan);

      queryMan = createQueryManager(info, mediator);
      mediator.setQueryMan(queryMan);

      transportMan = createTransportManager(info, mediator);
      mediator.setTransportMan(transportMan);

      endpointMan = createEndpointManager(info, mediator);
      mediator.setEndpointMan(endpointMan);

      return mediator;
    }
  }

  private DbmsMediator createMediator(DbmsInfo info) throws AppDbException {
    synchronized (engineLock) {
      String compClassname;
      DbmsMediator mediator;

      logger.info("Mediator Creation");

      if (info == null) {
        throw new AppDbException("DBMS Info is NULL");
      }

      compClassname = info.getMediatorClassname();

      if (compClassname == null || compClassname.trim().length() == 0) {
        throw new AppDbException("Mediator Classname NOT Set!");
      }

      mediator = (DbmsMediator) createDbmsComponent(compClassname.trim(), info, null);
      mediator.setDbmsEngine(this);

      return mediator;
    }
  }

  private DbmsSecurityManager createSecurityManager(DbmsInfo info, DbmsMediator mediator) throws AppDbException {
    synchronized (engineLock) {
      String compClassname;
      DbmsSecurityManager secMan;
      DbmsAuthenticationManager authenticationMan;
      DbmsAuthorizationManager authorizationMan;

      logger.info("Storage Manager Creation");

      if (info == null) {
        throw new AppDbException("DBMS Info is NULL");
      }

      if (mediator == null) {
        throw new AppDbException("DBMS Mediator is NULL");
      }

      compClassname = info.getSecurityManagerClassname();

      if (compClassname == null || compClassname.trim().length() == 0) {
        throw new AppDbException("Security Manager Classname NOT Set!");
      }

      authenticationMan = createAuthenticationManager(info, mediator);

      if (authenticationMan == null) {
        throw new AppDbException("Creation of Authentication Manager Failed with NULL Returned!");
      }

      authorizationMan = createAuthorizationManager(info, mediator);

      if (authorizationMan == null) {
        throw new AppDbException("Creation of Authorization Manager Failed with NULL Returned!");
      }

      secMan = (DbmsSecurityManager) createDbmsComponent(compClassname.trim(), info, mediator);
      secMan.setAuthenticationManager(authenticationMan);
      secMan.setAuthorizationManager(authorizationMan);

      return secMan;
    }
  }

  private DbmsAuthenticationManager createAuthenticationManager(DbmsInfo info, DbmsMediator mediator) throws AppDbException {
    synchronized (engineLock) {
      String compClassname;
      DbmsAuthenticationManager authenticationMan;

      logger.info("Authentication Manager Creation");

      if (info == null) {
        throw new AppDbException("DBMS Info is NULL");
      }

      if (mediator == null) {
        throw new AppDbException("DBMS Mediator is NULL");
      }

      compClassname = info.getAuthenticationManagerClassname();

      if (compClassname == null || compClassname.trim().length() == 0) {
        throw new AppDbException("Authentication Manager Classname NOT Set!");
      }

      authenticationMan = (DbmsAuthenticationManager) createDbmsComponent(compClassname.trim(), info, mediator);

      return authenticationMan;
    }
  }

  private DbmsAuthorizationManager createAuthorizationManager(DbmsInfo info, DbmsMediator mediator) throws AppDbException {
    synchronized (engineLock) {
      String compClassname;
      DbmsAuthorizationManager authorizationMan;

      logger.info("Authorization Manager Creation");

      if (info == null) {
        throw new AppDbException("DBMS Info is NULL");
      }

      if (mediator == null) {
        throw new AppDbException("DBMS Mediator is NULL");
      }

      compClassname = info.getAuthorizationManagerClassname();

      if (compClassname == null || compClassname.trim().length() == 0) {
        throw new AppDbException("Authorization Manager Classname NOT Set!");
      }

      authorizationMan = (DbmsAuthorizationManager) createDbmsComponent(compClassname.trim(), info, mediator);

      return authorizationMan;
    }
  }

  private DbmsStorageManager createStorageManager(DbmsInfo info, DbmsMediator mediator) throws AppDbException {
    synchronized (engineLock) {
      String compClassname;
      DbmsStorageManager storeMan;

      logger.info("Storage Manager Creation");

      if (info == null) {
        throw new AppDbException("DBMS Info is NULL");
      }

      if (mediator == null) {
        throw new AppDbException("DBMS Mediator is NULL");
      }

      compClassname = info.getStorageManagerClassname();

      if (compClassname == null || compClassname.trim().length() == 0) {
        throw new AppDbException("Storage Manager Classname NOT Set!");
      }

      storeMan = (DbmsStorageManager) createDbmsComponent(compClassname.trim(), info, mediator);

      return storeMan;
    }
  }

  private DbmsSessionManager createSessionManager(DbmsInfo info, DbmsMediator mediator) throws AppDbException {
    synchronized (engineLock) {
      String compClassname;
      DbmsSessionManager sessMan;

      logger.info("Session Manager Creation");

      if (info == null) {
        throw new AppDbException("DBMS Info is NULL");
      }

      if (mediator == null) {
        throw new AppDbException("DBMS Mediator is NULL");
      }

      compClassname = info.getSessionManagerClassname();

      if (compClassname == null || compClassname.trim().length() == 0) {
        throw new AppDbException("Session Manager Classname NOT Set!");
      }

      sessMan = (DbmsSessionManager) createDbmsComponent(compClassname.trim(), info, mediator);

      return sessMan;
    }
  }

  private DbmsQueryManager createQueryManager(DbmsInfo info, DbmsMediator mediator) throws AppDbException {
    synchronized (engineLock) {
      String compClassname;
      DbmsQueryManager qMan;

      logger.info("Query Manager Creation");

      if (info == null) {
        throw new AppDbException("DBMS Info is NULL");
      }

      if (mediator == null) {
        throw new AppDbException("DBMS Mediator is NULL");
      }

      compClassname = info.getQueryManagerClassname();

      if (compClassname == null || compClassname.trim().length() == 0) {
        throw new AppDbException("Query Manager Classname NOT Set!");
      }

      qMan = (DbmsQueryManager) createDbmsComponent(compClassname.trim(), info, mediator);

      return qMan;
    }
  }

  private DbmsTransportManager createTransportManager(DbmsInfo info, DbmsMediator mediator) throws AppDbException {
    synchronized (engineLock) {
      String compClassname;
      DbmsTransportManager transMan;

      logger.info("Transport Manager Creation");

      if (info == null) {
        throw new AppDbException("DBMS Info is NULL");
      }

      if (mediator == null) {
        throw new AppDbException("DBMS Mediator is NULL");
      }

      compClassname = info.getTransportManagerClassname();

      if (compClassname == null || compClassname.trim().length() == 0) {
        throw new AppDbException("Transport Manager Classname NOT Set!");
      }

      transMan = (DbmsTransportManager) createDbmsComponent(compClassname.trim(), info, mediator);

      return transMan;
    }
  }

  private DbmsEndpointManager createEndpointManager(DbmsInfo info, DbmsMediator mediator) throws AppDbException {
    synchronized (engineLock) {
      String compClassname;
      DbmsEndpointManager endPtMan;

      logger.info("Endpoint Manager Creation");

      if (info == null) {
        throw new AppDbException("DBMS Info is NULL");
      }

      if (mediator == null) {
        throw new AppDbException("DBMS Mediator is NULL");
      }

      compClassname = info.getEndpointManagerClassname();

      if (compClassname == null || compClassname.trim().length() == 0) {
        throw new AppDbException("Endpoint Manager Classname NOT Set!");
      }

      endPtMan = (DbmsEndpointManager) createDbmsComponent(compClassname.trim(), info, mediator);

      return endPtMan;
    }
  }

}
