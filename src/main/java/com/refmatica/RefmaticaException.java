/**
 * Created Sep 4, 2024
 */
package com.refmatica;

import com.ilardi.systems.util.IlardiSystemsException;

/**
 * @author robert.ilardi
 */

public class RefmaticaException extends IlardiSystemsException {

  public RefmaticaException() {
    super();
  }

  /**
   * @param message
   */
  public RefmaticaException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public RefmaticaException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public RefmaticaException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public RefmaticaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
