/**
 * Created Sep 4, 2024
 */
package com.refmatica.cmds;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.refmatica.RefmaticaException;
import com.refmatica.Version;

import io.ilardi.ApplicationContext;
import io.ilardi.IlardiException;
import io.ilardi.IlardiStringUtils;

/**
 * @author robert.ilardi
 */

public class RefmaticaCmdLocator {

  private static final Logger logger = LogManager.getLogger(RefmaticaCmdLocator.class);

  private static final String PROP_AVAILABLE_COMMAND_EXECUTABLES = "com.refmatica.cmds.availableCommandExecutables";

  private static RefmaticaCmdLocator instance = null;

  private final Object locatorLock;

  private volatile boolean locatorInited = false;

  private ApplicationContext appContext;

  private String[] availableCmdNames;

  private final HashMap<String, RefmaticaCmdKeyword> cmdAliasMap;

  private final HashMap<RefmaticaCmdKeyword, Class<? extends RefmaticaCmd>> cmdClassRegistry;

  private RefmaticaCmdLocator() throws RefmaticaException {
    locatorLock = new Object();

    cmdAliasMap = new HashMap<>();
    cmdClassRegistry = new HashMap<>();

    init();
  }

  public static synchronized RefmaticaCmdLocator getLocator() throws RefmaticaException {
    if (instance == null) {
      instance = new RefmaticaCmdLocator();
    }

    return instance;
  }

  private void init() throws RefmaticaException {
    synchronized (locatorLock) {
      try {
        if (locatorInited) {
          logger.warn("Warming?!?!? " + Version.APP_SHORT_NAME + " Command Locator Already Inited? Check Engine Light On!");
        }
        else {
          logger.debug("Initializing " + Version.APP_SHORT_NAME + " Command Locator...");

          appContext = ApplicationContext.getInstance();

          readProperties();

          registerCommandClasses();
        }
      } // End try block
      catch (Exception e) {
        throw new RefmaticaException("An error occurred while attempting to Initialize Command Locator! System Message: " + e.getMessage(), e);
      }
    } // End Synchronized Block
  }

  private void readProperties() throws IlardiException {
    String tmp;

    tmp = appContext.getAppProperty(PROP_AVAILABLE_COMMAND_EXECUTABLES);
    availableCmdNames = tmp.split(",");
    availableCmdNames = IlardiStringUtils.trimAllElements(availableCmdNames);
  }

  private void registerCommandClasses() {
    String cmdName, cmdAlias;
    RefmaticaCmdKeyword cmdKeyword;
    String[] cmdAliases;
    Class<? extends RefmaticaCmd> cmdClass;

    logger.debug("Registering Available Command Classes:");

    if (availableCmdNames != null && availableCmdNames.length > 0) {
      for (int i = 0; i < availableCmdNames.length; i++) {
        cmdName = availableCmdNames[i];
        cmdName = cmdName.trim();

        logger.debug("Registering Command Class for: " + cmdName);

        cmdKeyword = RefmaticaCmdKeyword.valueOf(cmdName);

        cmdAliases = cmdKeyword.getCmdAliases();

        cmdClass = cmdKeyword.getCmdClass();

        // Add the Command Keyword itself as an alias
        cmdAliasMap.put(cmdKeyword.name().toUpperCase(), cmdKeyword);

        // Add Command Keyword Aliases
        for (int j = 0; j < cmdAliases.length; j++) {
          cmdAlias = cmdAliases[j];
          cmdAlias = cmdAlias.toUpperCase();
          cmdAliasMap.put(cmdAlias, cmdKeyword);
        }

        // Register Command Class
        cmdClassRegistry.put(cmdKeyword, cmdClass);
      }
    }
  }

  public RefmaticaCmd locateCommand(String cmdStr) throws RefmaticaException {
    RefmaticaCmdKeyword cmdKeyword;
    Class<? extends RefmaticaCmd> cmdClass;
    Constructor<? extends RefmaticaCmd> construct;
    RefmaticaCmd cmd = null;

    try {
      logger.debug("Attempting to Locate Command for Input: " + cmdStr);

      cmdKeyword = locateCommandKeyword(cmdStr);

      if (cmdKeyword != null) {
        cmdClass = cmdKeyword.getCmdClass();

        construct = cmdClass.getConstructor();

        cmd = construct.newInstance();
        cmd.setKeyword(cmdKeyword);
        cmd.init();
      }
      else {
        logger.debug("NULL Command Keyword Deteted, Skipping Location Process!");
      }

      return cmd;
    } // End try block
    catch (Exception e) {
      throw new RefmaticaException("An error occurred while attempting to Locate Command for Command String Input: " + cmdStr + " ; System Message: " + e.getMessage(), e);
    }
  }

  private RefmaticaCmdKeyword locateCommandKeyword(String cmdStr) {
    RefmaticaCmdKeyword cmdKeyword = null;
    String cmdName;

    if (cmdStr != null) {
      logger.debug("Attempting to Locate Command Keyword for Input: " + cmdStr);

      cmdName = parseCommandName(cmdStr);
      cmdName = normalizeCommandName(cmdName);

      cmdKeyword = cmdAliasMap.get(cmdName);

      if (cmdKeyword != null) {
        logger.debug("Found Command Keyword: " + cmdKeyword + " for Input: " + cmdStr);
      }
      else {
        logger.debug("Command Keyword NOT Found for Input: " + cmdStr);
      }
    }
    else {
      logger.debug("Cannot Attempt Command Location for NULL Command String Input!");
    }

    return cmdKeyword;
  }

  private String parseCommandName(String cmdStr) {
    String cmdName = null;
    String[] tokens;

    if (cmdStr != null) {
      cmdStr = cmdStr.trim();

      if (IlardiStringUtils.startsWithQuoteChar(cmdStr)) {
        // Starts with Quote Char
        // Use Smart Quote Split to Split on First Whitespace Char
        tokens = IlardiStringUtils.smartQuoteSplit(cmdName, true, ' ', '\t');

        if (tokens != null && tokens.length > 0) {
          cmdName = tokens[0];
        }
      }
      else {
        // Does not start with Quote Split on First Whitespace Char
        tokens = IlardiStringUtils.splitOnAnyWhitespace(cmdName, true);

        if (tokens != null && tokens.length > 0) {
          cmdName = tokens[0];
        }
      }
    }

    return cmdName;
  }

  private String normalizeCommandName(String cmdName) {
    return (cmdName != null ? cmdName.trim().toUpperCase() : null);
  }

}
