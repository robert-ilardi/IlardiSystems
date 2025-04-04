/**
 * Created Sep 4, 2024
 */
package com.refmatica;

import com.ilardi.systems.util.IlardiSystemsRuntimeException;

/**
 * @author robert.ilardi
 */

public class RefmaticaRuntimeException extends IlardiSystemsRuntimeException {

  public RefmaticaRuntimeException() {
    super();
  }

  /**
   * @param message
   */
  public RefmaticaRuntimeException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public RefmaticaRuntimeException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public RefmaticaRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public RefmaticaRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
