/**
 * Created Sep 5, 2024
 */
package io.rcpi;

import com.ilardi.systems.IlardiSystemsException;

/**
 * @author robert.ilardi
 */

public class RcpiException extends IlardiSystemsException {

  public RcpiException() {
    super();
  }

  /**
   * @param message
   */
  public RcpiException(String message) {
    super(message);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param cause
   */
  public RcpiException(Throwable cause) {
    super(cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param message
   * @param cause
   */
  public RcpiException(String message, Throwable cause) {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public RcpiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    // TODO Auto-generated constructor stub
  }

}
