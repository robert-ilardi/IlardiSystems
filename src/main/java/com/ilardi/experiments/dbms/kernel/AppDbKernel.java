/**
 * Created Mar 28, 2021
 */
package com.ilardi.experiments.dbms.kernel;

import java.util.Properties;

import com.ilardi.experiments.dbms.AppDbException;
import com.ilardi.experiments.dbms.engine.DbmsEngine;
import com.ilardi.experiments.dbms.engine.DbmsEngineFactory;
import com.ilardi.experiments.dbms.engine.DbmsEngineFactoryFactory;
import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author robert.ilardi
 *
 */

public final class AppDbKernel {

  private static final Logger logger = LogUtil.getInstance().getLogger(AppDbKernel.class);

  private static final String PROP_KERNEL_INSTANCE_NAME = "AppDb.Kernel.InstanceName";

  private static AppDbKernel kernelInstance = null;

  private final Object kernelLock;

  private volatile boolean booted = false;

  private Properties appDbProperties;

  private String kernelInstanceName;

  private DbmsEngine dbEngine;

  private AppDbKernel() {
    kernelLock = new Object();
  }

  public static synchronized AppDbKernel getKernelInstance() {
    if (kernelInstance == null) {
      kernelInstance = new AppDbKernel();
    }

    return kernelInstance;
  }

  public void setProperties(Properties appDbProperties) {
    this.appDbProperties = appDbProperties;
  }

  public String getKernelInstanceName() {
    return kernelInstanceName;
  }

  public void boot() throws AppDbException {
    synchronized (kernelLock) {
      if (booted) {
        return;
      }

      logger.info("Booting AppDb Kernel");

      if (appDbProperties == null) {
        throw new AppDbException("AppDb Properties are NULL!");
      }

      kernelInstanceName = appDbProperties.getProperty(PROP_KERNEL_INSTANCE_NAME);

      if (kernelInstanceName == null) {
        throw new AppDbException("AppDb Kernel Instance Name is NULL!");
      }

      kernelInstanceName = kernelInstanceName.trim();

      if (kernelInstanceName.length() == 0) {
        throw new AppDbException("AppDb Kernel Instance Name is Empty String!");
      }

      createDbmsEngine();

      startDbmsEngine();

      booted = true;
      kernelLock.notifyAll();
    }
  }

  public void shutdown() throws AppDbException {
    synchronized (kernelLock) {
      if (!booted) {
        return;
      }

      logger.info("Shutting Down AppDb Kernel");

      stopDbmsEngine();

      booted = false;
      kernelLock.notifyAll();
    }
  }

  public void waitWhileRunning() throws AppDbException {
    logger.info("Waiting while AppDb is Running");
    dbEngine.waitWhileEngineRunning();
  }

  /*
   * public void waitWhileBooted() throws AppDbException { synchronized
   * (kernelLock) { try {
   * logger.info("Entering Waiting State on Kernel Booted Status");
   * 
   * while (booted) { kernelLock.wait(); } } catch (Exception e) { throw new
   * AppDbException("Error while Waiting on Kernel Booted State. System Message: "
   * + e.getMessage(), e); }
   * 
   * logger.info("Exiting Waiting State on Kernel Booted Status"); } }
   */

  private void createDbmsEngine() throws AppDbException {
    synchronized (kernelLock) {
      DbmsEngineFactoryFactory deAbstractFactory;
      DbmsEngineFactory deFactory;

      logger.info("Creating DBMS Engine");

      deAbstractFactory = DbmsEngineFactoryFactory.getInstance();

      deFactory = deAbstractFactory.getDbmsEngineFactory(appDbProperties);

      dbEngine = deFactory.createDbmsEngine(appDbProperties);

      dbEngine.createEngine();
    }
  }

  private void startDbmsEngine() throws AppDbException {
    synchronized (kernelLock) {
      logger.info("Starting DBMS Engine");
      dbEngine.startEngine();
    }
  }

  private void stopDbmsEngine() throws AppDbException {
    synchronized (kernelLock) {
      logger.info("Stopping DBMS Engine");
      dbEngine.stopEngine();
    }
  }

}
