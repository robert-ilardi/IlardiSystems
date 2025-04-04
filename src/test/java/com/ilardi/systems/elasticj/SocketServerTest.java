/**
 * Created Jan 8, 2021
 */
package com.ilardi.systems.elasticj;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.ilardi.systems.net.IoType;
import com.ilardi.systems.net.SocketInfo;
import com.ilardi.systems.net.SocketType;
import com.ilardi.systems.net.server.SocketServer;

/**
 * @author rilardi
 *
 */

@TestInstance(Lifecycle.PER_CLASS)
class SocketServerTest {

  private static final Logger logger = LogManager.getLogger(SocketServerTest.class);

  private SocketServer ss;

  @BeforeAll
  void setUp() throws Exception {
    SocketInfo sInfo;

    logger.debug("setUp()");

    sInfo = new SocketInfo();
    sInfo.setPort(1979);
    sInfo.setSocketType(SocketType.TCP);
    sInfo.setIoType(IoType.BLOCKING_IO);

    ss = new SocketServer();

    ss.init();
  }

  @AfterAll
  void tearDown() throws Exception {
    logger.debug("tearDown()");

    if (ss != null) {
      ss.destroy();
    }
  }

  @Test
  void testSocketServer() {
    logger.debug("testSocketServer()");
    assertTrue(true);
  }

}
