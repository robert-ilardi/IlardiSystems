/**
 * Created Jun 16, 2024
 */
package com.ilardi.systems;

import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ilardi.systems.ploader.ProgramLoader;
import com.ilardi.systems.util.ApplicationContext;
import com.ilardi.systems.util.IlardiSystemsException;

/**
 * @author robert.ilardi
 *
 */

public class SuperMain {

  private static final Logger logger = LogManager.getLogger(SuperMain.class);

  private ProgramLoader pLoader;

  public SuperMain(String programFriendlyName, String[] programArgs) throws IlardiSystemsException {
    logger.debug("Creating PpogramLoader Instance");
    pLoader = ProgramLoader.getInstance(programFriendlyName, programArgs);
  }

  public void startProgram() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, IlardiSystemsException {
    pLoader.startProgram();
  }

  public void stopProgram() throws IlardiSystemsException {
    pLoader.stopProgram();
  }

  public void destroyProgram() throws IlardiSystemsException {
    pLoader.destroyProgram();
  }

  public boolean isProgramInited() {
    return pLoader.isProgramInited();
  }

  public boolean isProgramStartSuccessful() {
    return pLoader.isProgramStartSuccessful();
  }

  public boolean isProgramRunning() {
    return pLoader.isProgramRunning();
  }

  public boolean isProgramAsync() {
    return pLoader.isProgramAsync();
  }

  public void waitWhileProgramRunning() throws IlardiSystemsException, InterruptedException {
    pLoader.waitWhileProgramRunning();
  }

  public void waitWhileProgramStarting() throws IlardiSystemsException, InterruptedException {
    pLoader.waitWhileProgramStarting();
  }

  public Exception getProgramLastException() {
    return pLoader.getProgramLastException();
  }

  public static boolean checkArgs(String[] args) {
    return args != null && args.length >= 1;
  }

  public static String getUsage() {
    StringBuilder sb = new StringBuilder();

    sb.append("Usage: java");

    sb.append(" -D");
    sb.append(ApplicationContext.SYS_PROP_DEFAULT_APP_PROPS_PATH);
    sb.append("=[APP-PROPERTIES-FILE-FILEPATH | APP-PROPERTIES-FILE-CLASSPATH] ");

    sb.append(SuperMain.class.getName());

    sb.append(" [PROGRAM-NAME] <PROGRAM-ARGS>");

    return sb.toString();
  }

  public static void printUsage() {
    String usage;

    usage = getUsage();

    logger.warn(usage);
  }

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    int exitCd;
    String programName;
    String[] programArgs;
    SuperMain sm = null;
    Exception lastEx;

    logger.debug("Starting Ilardi Systems **Super** Main Program...");

    try {
      if (!checkArgs(args)) {
        exitCd = 1;
        printUsage();
      }
      else {
        programName = args[0];
        programName.trim();

        programArgs = new String[args.length - 1];

        if (programArgs.length > 0) {
          System.arraycopy(args, 1, programArgs, 0, programArgs.length);
        }

        sm = new SuperMain(programName, programArgs);

        sm.startProgram();

        if (sm.isProgramAsync()) {
          sm.waitWhileProgramStarting();
          sm.waitWhileProgramRunning();
        }

        lastEx = sm.getProgramLastException();

        if (lastEx != null) {
          throw lastEx;
        }

        exitCd = 0;
      }
    } // End try block
    catch (Exception e) {
      exitCd = 1;
      logger.catching(e);

      if (e instanceof InterruptedException) {
        logger.warn("Interrupted! System Message: " + e.getMessage(), e);
        Thread.currentThread().interrupt();
      }
    }
    finally {
      if (sm != null && sm.isProgramRunning()) {
        sm.stopProgram();
      }

      if (sm != null) {
        sm.destroyProgram();
      }
    }

    logger.debug("Exit Status = " + exitCd);
    System.exit(exitCd);
  }

}
