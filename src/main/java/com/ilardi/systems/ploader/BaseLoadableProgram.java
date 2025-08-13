/**
 * Created Jun 25, 2024
 */
package com.ilardi.systems.ploader;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ilardi.systems.IlardiSystemsException;
import com.ilardi.systems.util.ApplicationContext;

/**
 * @author robert.ilardi
 *
 */

public abstract class BaseLoadableProgram implements LoadableProgram {

  private static final Logger logger = LogManager.getLogger(BaseLoadableProgram.class);

  private static final String JMX_ROOT_NAME = "IlardiSystems.ProgramLoader.LoadableProgram";
  private static final String JMX_TYPE = "Management";

  protected ApplicationContext appContext;

  protected final Object programLock;

  private boolean programAsync;
  private String programName;
  private ProgramLoader programLoader;

  private String instanceName;

  private volatile boolean programStarting;
  private volatile boolean programRunning;
  private volatile boolean programInited;
  private volatile boolean programStartSuccessful;

  private MBeanServer jmxServer;
  private ObjectName jmxObjName;
  private ProgramLoaderJmxMBean jmxMbean;

  protected BaseLoadableProgram(boolean programAsync, String programName, ProgramLoader programLoader) {
    programLock = new Object();

    programInited = false;
    programStarting = false;
    programStartSuccessful = false;
    programRunning = false;

    this.programAsync = programAsync;
    this.programName = programName;
    this.programLoader = programLoader;
  }

  public String getInstanceName() {
    return instanceName;
  }

  public void setInstanceName(String instanceName) {
    this.instanceName = instanceName;
  }

  public String getProgramName() {
    return programName;
  }

  @Override
  public void init() throws IlardiSystemsException {
    synchronized (programLock) {
      logger.debug("Initializing Ilardi Systems Loadable Program...");

      baseProgramInit();

      setProgramInited(true);
    }
  }

  protected void baseProgramInit() throws IlardiSystemsException {
    synchronized (programLock) {
      try {
        logger.debug("Entering Base Init Routine...");

        appContext = ApplicationContext.getInstance();

        readProperties();

        setupJmx();
      }
      catch (Exception e) {
        throw new IlardiSystemsException("An error occurred while attempting to Initialize Base Program! System Message: " + e.getMessage(), e);
      }
    }
  }

  protected void readProperties() {
    /*
     * Default implementation in the base class is empty because currently we don't
     * have any properties to read to be able to initialize the base class. However
     * we provided the hook so a developer who is implementing a LoadableProgram can
     * simply override this method without having to override the program init flow
     * if the only reason to do so is to load properties needed not for the init
     * process but for later in the program's execution. Also, there is a good
     * possibility that enhancements to the base class will require additional
     * config during the initialization process flow.
     */
  }

  private void setupJmx() throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
    StringBuilder sb;
    String jmxNmStr;
    ProgramLoaderJmx mbImpl;

    sb = new StringBuilder();
    sb.append(JMX_ROOT_NAME);
    sb.append(":type=");
    sb.append(JMX_TYPE);

    sb.append(",name=");
    sb.append(programName);

    if (instanceName != null && !instanceName.isBlank()) {
      sb.append(",instance=");
      sb.append(instanceName.trim());
    }

    jmxNmStr = sb.toString();

    logger.debug("Setting Up JMX Management for: " + jmxNmStr);

    mbImpl = new ProgramLoaderJmx();
    mbImpl.setProgramLoader(programLoader);
    mbImpl.setProgram(this);

    jmxMbean = mbImpl;

    jmxServer = ManagementFactory.getPlatformMBeanServer();
    jmxObjName = new ObjectName(jmxNmStr);

    jmxServer.registerMBean(mbImpl, jmxObjName);
  }

  @Override
  public void setProgramInited(boolean programInited) {
    synchronized (programLock) {
      this.programInited = programInited;
      programLock.notifyAll();
    }
  }

  @Override
  public void setProgramStarting(boolean programStarting) {
    synchronized (programLock) {
      this.programStarting = programStarting;
      programLock.notifyAll();
    }
  }

  @Override
  public void setProgramRunning(boolean programRunning) {
    synchronized (programLock) {
      this.programRunning = programRunning;
      programLock.notifyAll();
    }
  }

  @Override
  public void setProgramStartSuccessful(boolean programStartSuccessful) {
    synchronized (programLock) {
      this.programStartSuccessful = programStartSuccessful;
      programLock.notifyAll();
    }
  }

  @Override
  public void destroy() throws IlardiSystemsException {
    /*
     * Default implementation in the base class is empty as there is nothing to that
     * requires a custom cleanup function.
     */
  }

  @Override
  public boolean isProgramInited() {
    synchronized (programLock) {
      return programInited;
    }
  }

  @Override
  public boolean isProgramStarting() {
    synchronized (programLock) {
      return programStarting;
    }
  }

  @Override
  public boolean isProgramStartSuccessful() {
    synchronized (programLock) {
      return programStartSuccessful;
    }
  }

  @Override
  public boolean isProgramRunning() {
    synchronized (programLock) {
      return programRunning;
    }
  }

  @Override
  public boolean isProgramAsync() {
    return programAsync;
  }

  @Override
  public void waitWhileProgramRunning() throws IlardiSystemsException, InterruptedException {
    synchronized (programLock) {
      if (isProgramAsync()) {
        while (programRunning) {
          programLock.wait();
        }
      }
    }
  }

  @Override
  public void waitWhileProgramStarting() throws IlardiSystemsException, InterruptedException {
    synchronized (programLock) {
      if (isProgramAsync()) {
        while (programStarting) {
          programLock.wait();
        }
      }
    }
  }

}
