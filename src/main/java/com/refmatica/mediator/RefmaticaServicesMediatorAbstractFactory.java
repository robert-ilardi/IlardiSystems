/**
 * Created Sep 5, 2024
 */
package com.refmatica.mediator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ilardi.systems.util.ApplicationContext;
import com.refmatica.RefmaticaException;
import com.refmatica.Version;

/**
 * @author robert.ilardi
 */

public class RefmaticaServicesMediatorAbstractFactory {

  private static final Logger logger = LogManager.getLogger(RefmaticaServicesMediatorAbstractFactory.class);

  private static final String PROP_DEFAULT_SERVICE_MEDIATOR_TYPE = "com.refmatica.mediator.factory.defaultServiceMediatorType";

  private static RefmaticaServicesMediatorAbstractFactory afInstance = null;

  private final Object afLock;

  private ApplicationContext appContext;

  private RefmaticaServicesMediatorAbstractFactory() throws RefmaticaException {
    try {
      logger.debug("Initializing " + Version.APP_SHORT_NAME + " Services Mediator Abstract Factory: " + this.getClass().getName());

      afLock = new Object();

      appContext = ApplicationContext.getInstance();

      readProperties();
    } // End try block
    catch (Exception e) {
      throw new RefmaticaException("An error occurred while attempting to Initialize Services Mediator Abstract Factory. System Message: " + e.getMessage(), e);
    }
  }

  public static synchronized RefmaticaServicesMediatorAbstractFactory getInstance() throws RefmaticaException {
    if (afInstance == null) {
      afInstance = new RefmaticaServicesMediatorAbstractFactory();
    }

    return afInstance;
  }

  private void readProperties() {
    String tmp;

  }

}
