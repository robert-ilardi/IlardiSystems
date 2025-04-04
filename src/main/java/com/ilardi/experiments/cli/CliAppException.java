/**
 * Created Apr 30, 2023
 */
package com.ilardi.experiments.cli;

import com.ilardi.systems.util.IlardiSystemsException;

/**
 * @author robert.ilardi
 *
 */

public class CliAppException extends IlardiSystemsException {

  public CliAppException() {
    super();
  }

  /**
   * @param message
   */
  public CliAppException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public CliAppException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public CliAppException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public CliAppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
