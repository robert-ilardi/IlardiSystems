/**
 * Created Sep 4, 2024
 */
package com.refmatica.cmds;

import java.io.Serializable;

/**
 * @author robert.ilardi
 */

public enum RefmaticaCmdKeyword implements Serializable {

  VERSION(VersionCmd.class, "VERSION", "VER"), HELP(HelpCmd.class, "HELP", "?");

  private final Class<? extends RefmaticaCmd> cmdClass;
  private final String[] cmdAliases;

  RefmaticaCmdKeyword(Class<? extends RefmaticaCmd> cmdClass, String... cmdAliases) {
    this.cmdClass = cmdClass;
    this.cmdAliases = cmdAliases;
  }

  public Class<? extends RefmaticaCmd> getCmdClass() {
    return cmdClass;
  }

  public String[] getCmdAliases() {
    return cmdAliases;
  }

}
