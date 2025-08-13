/**
 * Created Apr 5, 2021
 */
package com.ilardi.systems.fileheap;

import com.ilardi.systems.IlardiSystemsException;

/**
 * @author robert.ilardi
 *
 */

public class FileHeapException extends IlardiSystemsException {

  public FileHeapException() {
    super();
  }

  /**
   * @param message
   */
  public FileHeapException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public FileHeapException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public FileHeapException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public FileHeapException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
