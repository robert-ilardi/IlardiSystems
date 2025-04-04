/**
 * Created Jan 7, 2021
 */
package com.ilardi.systems.net;

/**
 * @author rilardi
 *
 */

public class Version {

  public static final String VERSION = "5.0.0";

  public static final String APP_NAME = "IlardiNet Socket Framework";

  public static final String DESCRIPTION = "IlardiNet Socket Framework is a Socket Networking Programming SDK for Java";

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
