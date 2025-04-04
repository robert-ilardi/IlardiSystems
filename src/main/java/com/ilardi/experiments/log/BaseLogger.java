/**
 * Created Jan 25, 2021
 */
package com.ilardi.experiments.log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author rilardi
 *
 */

public abstract class BaseLogger implements Logger {

  public BaseLogger() {}

  @Override
  public void error(Throwable t) {
    String mesg = getStackTraceString(t);
    error(mesg);
  }

  @Override
  public void fatal(Throwable t) {
    String mesg = getStackTraceString(t);
    fatal(mesg);
  }

  protected String getCallingMethodName() {
    String callingMethod = null;

    StackTraceElement[] steArr = Thread.currentThread().getStackTrace();

    if (steArr.length >= 4) {
      callingMethod = steArr[3].getMethodName();
    }

    return callingMethod;
  }

  protected String getCallingClassName() {
    String callingClass = null;

    StackTraceElement[] steArr = Thread.currentThread().getStackTrace();

    if (steArr.length >= 4) {
      callingClass = steArr[3].getClassName();
    }

    return callingClass;
  }

  protected String getStackTraceString(Throwable t) {
    ByteArrayOutputStream baos = null;
    PrintStream ps = null;
    String temp = null;

    try {
      baos = new ByteArrayOutputStream();
      ps = new PrintStream(baos);

      t.printStackTrace(ps);
      temp = baos.toString();
    }
    catch (Exception ie) {
      t.printStackTrace();
    }
    finally {
      try {
        if (ps != null) {
          ps.close();
        }
      }
      catch (Exception ie) {}

      try {
        if (baos != null) {
          baos.close();
        }
      }
      catch (Exception ie) {}
    }

    return temp;
  }

}
