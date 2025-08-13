/**
 * Created Oct 12, 2021
 */
package com.ilardi.systems.gte;

import com.ilardi.systems.IlardiSystemsException;

/**
 * @author robert.ilardi
 *
 */

public class GteException extends IlardiSystemsException {

  public GteException() {
    super();
  }

  /**
   * @param message
   */
  public GteException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public GteException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public GteException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public GteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
