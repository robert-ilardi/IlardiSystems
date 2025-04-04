/*
 * Created on May 18, 2005
 */

/*
 * Copyright 2007 Robert C. Ilardi
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ilardi.systems.util;

import java.io.File;
import java.net.InetAddress;

/**
 * @author rilardi
 */

public class IlardiSystemUtils {

  private static final String SYSPROP_OS_NAME = "os.name";
  private static final String SYSPROP_OS_VERSION = "os.version";

  private static final String SYSPROP_JAVA_VERSION = "java.version";
  private static final String SYSPROP_JAVA_VENDOR = "java.vendor";

  private static final String SYSPROP_WORKING_DIRECTORY = "user.dir";
  private static final String SYSPROP_HOME_DIRECTORY = "user.home";

  private static final double MEGABYTE_SIZE = 1048576.0d;

  public static String getWorkingDirectory() {
    return getDirectory(SYSPROP_WORKING_DIRECTORY);
  }

  public static String getHomeDirectory() {
    return getDirectory(SYSPROP_HOME_DIRECTORY);
  }

  public static String getOperatingSystemName() {
    return System.getProperty(SYSPROP_OS_NAME);
  }

  public static String getOperatingSystemVersion() {
    return System.getProperty(SYSPROP_OS_VERSION);
  }

  public static String getJavaVersion() {
    return System.getProperty(SYSPROP_JAVA_VERSION);
  }

  public static String getJavaVendor() {
    return System.getProperty(SYSPROP_JAVA_VENDOR);
  }

  public static String getDirectory(String path) {
    File file;
    StringBuffer dir = new StringBuffer();

    file = new File(System.getProperty(path));
    dir.append(file.getAbsolutePath());

    if (!dir.toString().endsWith(File.separator)) {
      dir.append(File.separator);
    }

    return dir.toString();
  }

  public static String getHostname() {
    String hostname = null;

    try {
      InetAddress lh = InetAddress.getLocalHost();
      hostname = lh.getHostName();
    }
    catch (Exception e) {}

    return hostname;
  }

  public static void sleep(int secs) {
    try {
      for (int i = 1; i <= secs; i++) {
        Thread.sleep(1000);
      }
    }
    catch (Exception e) {}
  }

  /*
   * Wraps Thread.sleep(int milliseconds) static method for quick and easy use
   */
  public static void sleepTight(long milliseconds) {
    try {
      Thread.sleep(milliseconds);
    }
    catch (Exception e) {}
  }

  public static boolean fileExists(String filePath) {
    File f;
    boolean exists = false;

    if (filePath != null) {
      f = new File(filePath);
      exists = f.exists();
    }

    return exists;
  }

  public static double getAvailableMemoryInMB() {
    Runtime rt = Runtime.getRuntime();
    double availableMem;
    long maxMem, freeMem, usedJvmMem;

    usedJvmMem = rt.totalMemory();
    maxMem = rt.maxMemory();
    freeMem = rt.freeMemory();
    availableMem = (maxMem - (usedJvmMem - freeMem)) / MEGABYTE_SIZE;

    return availableMem;
  }

  public static double getTotalMemoryInMB() {
    Runtime rt = Runtime.getRuntime();
    double maxMem = rt.maxMemory() / MEGABYTE_SIZE;
    return maxMem;
  }

}
