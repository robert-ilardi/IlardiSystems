/**
 * Created Sep 4, 2024
 */
package com.refmatica.mediator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.refmatica.Version;

import io.ilardi.ApplicationContext;

/**
 * @author robert.ilardi
 */

public abstract class RefmaticaServicesMediator {

  private static final Logger logger = LogManager.getLogger(RefmaticaServicesMediator.class);

  protected ApplicationContext appContext;

  protected final Object mediatorLock;

  public RefmaticaServicesMediator() {
    mediatorLock = new Object();

    logger.debug("Initializing " + Version.APP_SHORT_NAME + " Mediator: " + this.getClass().getName());

  }

}
