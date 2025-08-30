/**
 * Created Jan 7, 2021
 */
package io.ilardi;

/**
 * @author rilardi
 *
 */

public class IlardiException extends Exception {

  public IlardiException() {
    super();
  }

  /**
   * @param message
   */
  public IlardiException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public IlardiException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public IlardiException(String message, Throwable cause) {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public IlardiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
