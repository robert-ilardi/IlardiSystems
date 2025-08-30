/**
 * Created Jan 31, 2021
 */
package io.ilardi;

/**
 * @author rilardi
 *
 */

public class IlardiRuntimeException extends RuntimeException {

  public IlardiRuntimeException() {
    super();
  }

  /**
   * @param message
   */
  public IlardiRuntimeException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public IlardiRuntimeException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public IlardiRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public IlardiRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
