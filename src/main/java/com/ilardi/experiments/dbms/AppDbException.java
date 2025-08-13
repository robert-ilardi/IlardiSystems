/**
 * Created Mar 28, 2021
 */
package com.ilardi.experiments.dbms;

import com.ilardi.systems.IlardiSystemsException;

/**
 * @author robert.ilardi
 *
 */

public class AppDbException extends IlardiSystemsException {

  public AppDbException() {
    super();
  }

  /**
   * @param message
   */
  public AppDbException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public AppDbException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public AppDbException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public AppDbException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
