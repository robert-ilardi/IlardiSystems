/**
 * Created Jan 8, 2021
 */
package com.ilardi.systems.net;

import com.ilardi.systems.util.IlardiSystemsException;

/**
 * @author rilardi
 *
 */

public class IlardiNetException extends IlardiSystemsException {

  public IlardiNetException() {
    super();
  }

  /**
   * @param message
   */
  public IlardiNetException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public IlardiNetException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public IlardiNetException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public IlardiNetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
