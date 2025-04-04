/**
 * Created Jan 23, 2021
 */
package com.ilardi.systems.net;

import java.time.format.DateTimeFormatter;

/**
 * @author rilardi
 *
 */

public abstract class BaseSocketFrameworkController implements SocketFrameworkController {

  protected final Object controllerLock;

  protected final DateTimeFormatter dtf;

  public BaseSocketFrameworkController() {
    controllerLock = new Object();

    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  }

}
