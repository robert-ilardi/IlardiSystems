/**
 * Created Jun 15, 2024
 */
package com.ilardi.systems.util;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

/**
 * @author rober
 */

class ApplicationContextTest {

  private static final Logger logger = LogManager.getLogger(ApplicationContextTest.class);

  @Test
  void test() throws IlardiSystemsException {
    ApplicationContext appContext;
    ApplicationMode appMode;

    appContext = ApplicationContext.getInstance();

    appMode = appContext.getApplicationMode();

    logger.debug("App Mode: " + appMode);

    assertNotEquals(appMode, ApplicationMode.DEFAULT);
  }

}
