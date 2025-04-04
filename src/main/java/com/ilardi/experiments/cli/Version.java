/**
 * Created Apr 30, 2023
 */
package com.ilardi.experiments.cli;

/**
 * @author rilardi
 *
 */

public class Version {

  public static final String VERSION = "0.9.0";

  public static final String APP_NAME = "CliApp";

  public static final String DESCRIPTION = "Command Line Interface Application Framework";

  public static final String COPYRIGHT = "Copyright (c) 2023 By: Robert C. Ilardi";

  public static String getVersionInfo() {
    StringBuilder sb = new StringBuilder();

    sb.append(APP_NAME);
    sb.append("\n");
    sb.append("Version: ");
    sb.append(VERSION);
    sb.append("\n");
    sb.append(DESCRIPTION);
    sb.append("\n");
    sb.append(COPYRIGHT);

    return sb.toString();
  }

  public Version() {}

  public static void main(String[] args) {
    int exitCd;
    String verInfo;

    try {
      exitCd = 0;

      verInfo = getVersionInfo();

      System.out.println(verInfo);
    }
    catch (Exception e) {
      exitCd = 1;
      e.printStackTrace();
    }

    System.exit(exitCd);
  }

}
