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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.roguelogic.sin.model.SinStackServiceSignature;

public class SinStackKernel {

  private static final String PROP_SERVICES_LIST_FILE = "ServiceListFile";

  private static SinStackKernel kernelInstance = null;

  private Properties stackProps;

  private String sinStackEtcPath;

  private Object runningLock;
  private boolean running;

  private boolean booted;

  private List<SinStackService> services;

  private SinStackKernel() {
    runningLock = new Object();
    running = false;
    booted = false;
  }

  // Yes I know the preferred mechanism to create
  // Signletons now is using a Enum, but
  // I don't like that way of implementing it.
  public static synchronized SinStackKernel getInstance() {
    if (kernelInstance == null) {
      kernelInstance = new SinStackKernel();
    }

    return kernelInstance;
  }

  public void boot(Properties stackProps, String sinStackEtcPath) throws SinStackException {
    synchronized (runningLock) {
      if (!running) {
        this.stackProps = stackProps;
        this.sinStackEtcPath = sinStackEtcPath;

        System.out.println("Booting SIN Stack Kernel...");

        initServices();

        if (services != null && !services.isEmpty()) {
          System.out.println("SIN Stack Kernel Boot Completed!");

          booted = true;
          running = true;
        }
        else {
          System.err.println("WARNING: SIN Stack Kernel Boot Completed, however NO Services were Loaded during boot. Please check your SIN Stack Configuration and try again. Bailing Out...");

          booted = false;
          running = false;
        }

        runningLock.notifyAll();
      }
    }
  }

  public void shutdown() throws SinStackException {
    synchronized (runningLock) {
      if (running) {
        shutdownServices();

        running = false;
        runningLock.notifyAll();
      }
    }
  }

  public void waitWhileRunning() throws InterruptedException {
    synchronized (runningLock) {
      while (running) {
        runningLock.wait();
      }
    }
  }

  private void initServices() throws SinStackException {
    List<SinStackServiceSignature> signatures;
    SinStackServiceSignature ssss;
    SinStackService sss;

    try {
      System.out.println("Initializing SIN Stack Services.");

      signatures = readServicesListFile();

      if (signatures != null && !signatures.isEmpty()) {
        services = new ArrayList<SinStackService>();

        for (int i = 0; i < signatures.size(); i++) {
          ssss = signatures.get(i);

          if (ssss != null) {
            sss = createService(ssss);

            if (sss != null) {
              initService(sss);

              services.add(sss);
            }
          }
        }
      }
    }
    catch (Exception e) {
      throw new SinStackException("An error occurred while attempting to Initialize SIN Stack Services! System Message: " + e.getMessage(), e);
    }
  }

  private List<SinStackServiceSignature> readServicesListFile() throws IOException {
    List<SinStackServiceSignature> ssssLst;
    SinStackServiceSignature ssss;
    String servicesListFile;
    FileInputStream fis = null;
    InputStreamReader isr = null;
    BufferedReader br = null;
    String line;

    try {
      servicesListFile = stackProps.getProperty(PROP_SERVICES_LIST_FILE);
      servicesListFile = servicesListFile.trim();
      servicesListFile = (new StringBuilder()).append(sinStackEtcPath).append("/").append(servicesListFile).toString();

      System.out.println("Loading Services List File: " + servicesListFile);

      fis = new FileInputStream(servicesListFile);
      isr = new InputStreamReader(fis);
      br = new BufferedReader(isr);

      ssssLst = new ArrayList<SinStackServiceSignature>();

      line = br.readLine();

      while (line != null) {
        line = line.trim();

        if (!line.startsWith("#")) {
          ssss = parseServicesListConfLine(line);

          if (ssss != null) {
            ssssLst.add(ssss);
          }
        }

        line = br.readLine();
      }

      return ssssLst;
    }
    finally {
      try {
        if (br != null) {
          br.close();
          br = null;
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }

      try {
        if (isr != null) {
          isr.close();
          isr = null;
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }

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

  private SinStackServiceSignature parseServicesListConfLine(String line) {
    SinStackServiceSignature ssss = null;
    String[] tokens;

    tokens = line.split("\\|", 3);

    ssss = new SinStackServiceSignature();

    ssss.setServiceName(tokens[0].trim());
    ssss.setServiceClassname(tokens[1].trim());

    if (tokens.length > 2) {
      ssss.setServiceInitOptions(tokens[2].trim());
    }

    return ssss;
  }

  @SuppressWarnings("unchecked")
  private SinStackService createService(SinStackServiceSignature ssss) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    SinStackService sss = null;
    Class<SinStackService> c;
    String cn;

    System.out.println("Creating Service: " + ssss);

    cn = ssss.getServiceClassname();

    c = (Class<SinStackService>) Class.forName(cn);

    sss = c.newInstance();

    sss.setStackProperties(stackProps);
    sss.setServiceSignature(ssss);

    return sss;
  }

  private void initService(SinStackService sss) throws SinStackException {
    sss.init();
    System.out.println("Initializing Service: " + sss);

  }

  private void shutdownServices() {
    // TODO Auto-generated method stub

  }

}
