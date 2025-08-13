/**
 * Created Sep 5, 2024
 */
package io.rcpi;

import com.ilardi.systems.IlardiSystemsRuntimeException;

/**
 * @author robert.ilardi
 */

public class RcpiRuntimeException extends IlardiSystemsRuntimeException {

  public RcpiRuntimeException() {
    super();
  }

  /**
   * @param message
   */
  public RcpiRuntimeException(String message) {
    super(message);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param cause
   */
  public RcpiRuntimeException(Throwable cause) {
    super(cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param message
   * @param cause
   */
  public RcpiRuntimeException(String message, Throwable cause) {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public RcpiRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    // TODO Auto-generated constructor stub
  }

}
