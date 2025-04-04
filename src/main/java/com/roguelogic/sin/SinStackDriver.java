/**
 * Created Jun 9, 2019
 * 
 * Copyright 2019 JavaSin@protonmail.com
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
 * 
 * 
 * 'Java SIN (The Original Developer of SIN) <javasin@protonmail.com>'
 * 
 * -----BEGIN PGP PUBLIC KEY BLOCK----- Version: GnuPG v1
 * 
 * mQENBFz875YBCADUoyRjG3ubhlz4MLcC+5TGDnI6/77dweSVHtGDzvF9SSD3X8io
 * Nxuu5cUSGlk6j5YBklIU+vPImtfDCYdqXpJnQH4tqt+qiIz3JvZ+uq88zSR4nUXu
 * 0Z58zNp7qOFQP3fIZhC5N4AaoL6oOOANiA9Ebv4qvU2Ve340xU5bZ66nqmJ72NIi
 * 4BxpfDiWNsS8LROupK/7M0qezopaAW/oz8FAolBgVv60fYqhpSbWJRVlz1NG4Dts
 * WTXxtcsr4Y17Rlqt2jKMMDxG7hw468k1h53meBcr50bzDRbvKCsegrUWM24FO0Fc
 * Vlh2hLfHKih4y7hF1/v8f6kk3Zjrud3KXsZxABEBAAG0QUphdmEgU0lOIChUaGUg
 * T3JpZ2luYWwgRGV2ZWxvcGVyIG9mIFNJTikgPGphdmFzaW5AcHJvdG9ubWFpbC5j
 * b20+iQE4BBMBAgAiBQJc/O+WAhsDBgsJCAcDAgYVCAIJCgsEFgIDAQIeAQIXgAAK
 * CRBRGfjZM0iumx4OB/4gnPB58a/5JoF4eEIh3Nrj92rfIua7KJYf7xUI7oiULX7B
 * kzz463N6UAiDaCbW9l2t1vHXHrmo5D16Z9/1h8zcKFA4gWWKQhqN4SnT47raaofp
 * BXvK7sG5Vxyy6W2B4iQK6fPy+A/AjQZRYX5VzPQ4361jAPJJnZ+ARYgW6tQ0R7JZ
 * qniKcBWc6ZyVoAW/RdSYJlc/kOZ0S9KIKxRHmNEEjHXzDoLuR8/ZHvQTOEhIk8eO
 * KmTc7WovC1s5AkgjaKBQDThqTJtMZJpFOJZTJ29d0PKLQZR4R/UecvDBhHli9vDu
 * vmkJ6kyTNatwhPSc7LJP3EK2RaAyv6kO5frs6oQOuQENBFz875YBCADi4+6bF6Vj
 * sNN9Biflb3InatfoCqPInkPYgIZTFyKK4c+/12FM8ZpaKJug+cmMQZtQhfggcNpL
 * 4YZnKRd71bZnObadnLUafinv1298T39abAYL56ljG4BDC3mcbnezz+Hl26qlheOr
 * xQLSS5U2+j//og3xjf4g6AMsMIp86KCj/Qabl8Ky/UPwwp933BTCBAoZBtDcJv5t
 * Yo+Ug7UAhqrutQy03MY2QQlIaLtpVhrwAhkJ8vL1oLI4CJXY/la0Q9BhBmdI6HOu
 * xFQmZvSO4TI+ndDBvbALq7BYIzvOMNUfqeCz0gjzdcwVchZQNm4Xto6cWbztQ3c1
 * eoL/JKT/ulvhABEBAAGJAR8EGAECAAkFAlz875YCGwwACgkQURn42TNIrpuDrggA
 * qXcbGezvTAzTmrjKIc6PKyQu1QaMXbSZXyXqjnzHNKv+d8M3pCIefATXWQX5a/PG
 * iNXifhdhooX/v4zcnkiI0K1jGFERzcVCioAeedJSViam/sCiIYQ9jaQhNpYgslbK
 * QwKexlgEPf/mVptRE2Aisz0aY62yRGPFNB3qEtdUvXwjKUq1issWM9z/2VUE87lT
 * uDrSAU5zQv0Ewsax8N3jJ84Ywp2egKhCKYav1P0tCqFN8EM560BLLOLemvb65sjL
 * TCPzpejmQOdVhQ7HwA/rV8YlfXnksZ4CbMggzcTiiyRNl17cRBTzO2BnPB8IT0cW
 * 5PEYfdys8G3+T1REFWaryg== =YAo8 -----END PGP PUBLIC KEY BLOCK-----
 * 
 * 
 */

package com.roguelogic.sin;

import java.io.FileInputStream;
import java.util.Properties;

public class SinStackDriver {

  private static final String DEFAULT_SIN_STACK_PROP_FILENAME = "sinstack.conf";

  private String sinStackEtcPath;
  private Properties stackProps;

  private SinStackKernel kernel = null;

  private final Object driverLock;

  private volatile boolean bootRan = false;
  private volatile boolean shutdownRan = false;

  public SinStackDriver() {
    driverLock = new Object();
    resetDriver();
  }

  private void setSinStackEtcPath(String sinStackEtcPath) {
    this.sinStackEtcPath = sinStackEtcPath;
  }

  private void resetDriver() {
    synchronized (driverLock) {
      bootRan = false;
      shutdownRan = false;
    }
  }

  private void boot() throws SinStackException {
    synchronized (driverLock) {
      if (!bootRan) {
        bootRan = true;

        Version.Print();

        loadStackProps();

        kernel = SinStackKernel.getInstance();

        kernel.boot(stackProps, sinStackEtcPath);
      }
    }
  }

  private void loadStackProps() throws SinStackException {
    FileInputStream fis = null;
    String stackPropFilename;

    try {
      System.out.println("Using SIN Stack ETC Directory: " + sinStackEtcPath);

      stackPropFilename = (new StringBuilder()).append(sinStackEtcPath).append("/").append(DEFAULT_SIN_STACK_PROP_FILENAME).toString();

      System.out.println("Loading SIN Stack Properties File: " + stackPropFilename);

      fis = new FileInputStream(stackPropFilename);

      stackProps = new Properties();
      stackProps.load(fis);
    }
    catch (Exception e) {
      throw new SinStackException("An error occurred while attempting to Load SIN Stack Properties! System Message: " + e.getMessage(), e);
    }
    finally {
      try {
        if (fis != null) {
          fis.close();
          fis = null;
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void waitWhileRunning() throws InterruptedException {
    if (kernel != null) {
      System.out.println("Entering Waiting State while SIN Stack is Running...");
      kernel.waitWhileRunning();
    }
  }

  private void shutdown() throws SinStackException {
    synchronized (driverLock) {
      if (!shutdownRan) {
        shutdownRan = true;

        if (kernel != null) {
          kernel.shutdown();
          kernel = null;
        }
      }
    }
  }

  public void addShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        try {
          shutdown();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  public static void main(String[] args) {
    SinStackDriver driver = null;
    int exitCd;
    String sinStackEtcPath;

    try {
      if (args.length == 1) {
        sinStackEtcPath = args[0].trim();
      }
      else {
        sinStackEtcPath = System.getProperty("user.dir");
      }

      driver = new SinStackDriver();

      driver.setSinStackEtcPath(sinStackEtcPath);

      driver.boot();

      driver.addShutdownHook();

      driver.waitWhileRunning();

      exitCd = 0;
    }
    catch (Exception e) {
      exitCd = 1;
      e.printStackTrace();
    }
    finally {
      try {
        if (driver != null) {
          driver.shutdown();
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }

    System.exit(exitCd);
  }

}
