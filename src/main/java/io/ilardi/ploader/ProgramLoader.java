/**
 * Created Jun 17, 2024
 */
package io.ilardi.ploader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.ilardi.ApplicationContext;
import io.ilardi.IlardiException;
import io.ilardi.IlardiStringUtils;

/**
 * @author robert.ilardi
 *
 */

public class ProgramLoader {

  private static final Logger logger = LogManager.getLogger(ProgramLoader.class);

  private static final String PROPS_PROGRAM_NAME_LIST = "ilardi.programLoader.programNameList";
  private static final String PROPS_PREFIX_PROGRAM_CONFIG = "ilardi.programLoader.programConfig";

  private static final String PROPS_PROGRAM_CLASSNAME = "programClassname";

  private ApplicationContext appContext;

  private volatile boolean inited;

  private String targetProgram;

  private ArrayList<String> programNames;

  private HashMap<String, ProgramConfig> programConfigMap;

  private LoadableProgram program;

  private Thread asyncProgramThread;
  private String[] programArgs;
  private Exception programLastException;

  private ProgramLoader(String targetProgram, String[] programArgs) {
    inited = false;
    program = null;

    this.targetProgram = targetProgram;
    this.programArgs = programArgs;
  }

  public static synchronized ProgramLoader getInstance(String targetProgram, String[] programArgs) throws IlardiException {
    ProgramLoader pLoader;

    pLoader = new ProgramLoader(targetProgram, programArgs);
    pLoader.init();

    return pLoader;
  }

  private synchronized void init() throws IlardiException {
    try {
      if (!inited) {
        appContext = ApplicationContext.getInstance();

        readProperties();

        loadProgramConfigMap();

        inited = true;
      }
      else {
        logger.warn("Program Loader Already Initialized!");
      }
    } // End try block
    catch (Exception e) {
      throw new IlardiException("Error while attempting to Initialize Program Loader! System Message: " + e.getMessage(), e);
    }
  }

  private void readProperties() throws IlardiException {
    String tmp;
    String[] tmpArr;

    logger.debug("Reading Program Loader Properties");

    tmp = appContext.getAppProperty(PROPS_PROGRAM_NAME_LIST);
    tmp = tmp.trim();

    tmpArr = tmp.split(",");
    programNames = new ArrayList<>();

    for (int i = 0; i < tmpArr.length; i++) {
      tmp = tmpArr[i];
      tmp = tmp.trim();
      programNames.add(tmp);
    }
  }

  private void loadProgramConfigMap() {
    String pName;
    ProgramConfig pConfig;

    logger.debug("Loading Program Configuration Map");

    programConfigMap = new HashMap<>();

    for (int i = 0; i < programNames.size(); i++) {
      pName = programNames.get(i);

      pConfig = loadProgramConfig(pName);

      programConfigMap.put(pName, pConfig);
    }
  }

  private ProgramConfig loadProgramConfig(String pName) {
    ProgramConfig pConfig;
    StringBuilder sb;
    String pcPropPrefix, pClassname;
    Properties props;

    logger.debug("Loading Program Configuration: " + pName);

    sb = new StringBuilder();
    sb.append(PROPS_PREFIX_PROGRAM_CONFIG);
    sb.append(".");
    sb.append(pName);
    sb.append(".");

    pcPropPrefix = sb.toString();

    props = appContext.getAppPropertiesByPropPrefix(pcPropPrefix);

    props = IlardiStringUtils.removePrefixFromPropNames(props, pcPropPrefix);

    pClassname = props.getProperty(PROPS_PROGRAM_CLASSNAME);

    pConfig = new ProgramConfig(pName, pClassname, props);

    return pConfig;
  }

  public boolean isProgramInited() {
    return program.isProgramInited();
  }

  public boolean isProgramStartSuccessful() {
    return program.isProgramStartSuccessful();
  }

  public boolean isProgramRunning() {
    return program.isProgramRunning();
  }

  public boolean isProgramAsync() {
    return program.isProgramAsync();
  }

  public void waitWhileProgramRunning() throws IlardiException, InterruptedException {
    program.waitWhileProgramRunning();
  }

  public void waitWhileProgramStarting() throws IlardiException, InterruptedException {
    program.waitWhileProgramStarting();
  }

  public Exception getProgramLastException() {
    return programLastException;
  }

  public void destroyProgram() throws IlardiException {
    program.destroy();
  }

  public void stopProgram() throws IlardiException {
    if (isProgramRunning()) {
      program.stopProgram();
    }
    else {
      logger.warn("Target Program NOT Running!");
    }
  }

  public void enableShutdownHook() {
    logger.debug("Enabling Shutdown Hook");

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        try {
          if (isProgramRunning()) {
            logger.debug("Running Shutdown Hook");
            stopProgram();
          }
        }
        catch (Exception e) {
          logger.catching(e);

          if (e instanceof InterruptedException) {
            logger.warn("Interrupted! System Message: " + e.getMessage(), e);
            Thread.currentThread().interrupt();
          }
        }
      }
    });
  }

  @SuppressWarnings("unchecked")
  private LoadableProgram loadProgram()
      throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IlardiException {
    LoadableProgram proggie;
    Class<? extends LoadableProgram> proggieClass;
    Constructor<? extends LoadableProgram> proggieConstructor;
    ProgramConfig proggieConfig;
    String proggieClassname;

    logger.debug("Loading Program: " + targetProgram);

    proggieConfig = programConfigMap.get(targetProgram);
    proggieClassname = proggieConfig.getProgramClassName();

    proggieClass = (Class<? extends LoadableProgram>) Class.forName(proggieClassname);
    proggieConstructor = proggieClass.getConstructor(String.class, ProgramLoader.class);

    proggie = proggieConstructor.newInstance(targetProgram, this);
    proggie.init();

    return proggie;
  }

  private void runProgramSync() throws IlardiException {
    logger.debug("Running Target Program within Calling Thread");
    program.startProgram(programArgs);
  }

  private void runProgramAsync() throws IlardiException {
    logger.debug("Is Async Program - Running Target Program in Separate Thread");
    asyncProgramThread = new Thread(asyncProgramRunner);
    asyncProgramThread.start();
  }

  private Runnable asyncProgramRunner = new Runnable() {
    @Override
    public void run() {
      try {
        program.startProgram(programArgs);
      }
      catch (Exception e) {
        logger.catching(e);

        if (e instanceof InterruptedException) {
          logger.warn("Interrupted! System Message: " + e.getMessage(), e);
          Thread.currentThread().interrupt();
        }
      }
    }
  };

  public void startProgram()
      throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IlardiException {
    if (program != null && isProgramRunning()) {
      logger.warn("Target Program Already Running!");
    }
    else {
      logger.debug(">> Starting Target Program: " + targetProgram);

      program = loadProgram();

      program.setProgramStarting(true);

      if (isProgramAsync()) {
        runProgramAsync();
      }
      else {
        runProgramSync();
      }
    }
  }

}
