/**
 * Created Feb 14, 2026
 */
package io.ilardi.quinn;

import io.ilardi.IlardiSystemsException;

/**
 * Author Kate Ilardi
 */

public class QuinnException extends IlardiSystemsException {

  public QuinnException() {
    super();
  }

  /**
   * @param message
   */
  public QuinnException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public QuinnException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public QuinnException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public QuinnException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
