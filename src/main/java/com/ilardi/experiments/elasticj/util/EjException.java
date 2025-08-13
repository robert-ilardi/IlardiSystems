/**
 * Created Jan 7, 2021
 */
package com.ilardi.experiments.elasticj.util;

import com.ilardi.systems.IlardiSystemsException;

/**
 * @author rilardi
 *
 */

public class EjException extends IlardiSystemsException {

  public EjException() {
    super();
  }

  /**
   * @param message
   */
  public EjException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public EjException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public EjException(String message, Throwable cause) {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public EjException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
