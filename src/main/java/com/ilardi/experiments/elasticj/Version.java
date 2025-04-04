/**
 * Created Jan 7, 2021
 */
package com.ilardi.experiments.elasticj;

/**
 * @author rilardi
 *
 */

public class Version {

  public static final String VERSION = "0.9.0";

  public static final String APP_NAME = "elasticJ";

  public static final String DESCRIPTION = "elasticJ is a Distributed Computing Parallel Processing Framework for Java";

  public static final String COPYRIGHT = "Copyright (c) 2021 By: Robert C. Ilardi";

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
