/**
 * Created Jan 24, 2021
 */
package com.ilardi.systems.net.intalk;

/**
 * @author rilardi
 *
 */

public class Version {

  public static final String VERSION = "6.0.0";

  public static final String APP_NAME = "IlardiNet Talk";

  public static final String DESCRIPTION = "IlardiNet Talk is an Application Level Network Protocol";

  public static final String COPYRIGHT = "Copyright (c) 1999 - 2021 By: Robert C. Ilardi";

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
