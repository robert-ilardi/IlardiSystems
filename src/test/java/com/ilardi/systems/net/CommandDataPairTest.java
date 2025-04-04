/**
 * Created Jun 15, 2024
 */
package com.ilardi.systems.net;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.ilardi.systems.net.intalk.CommandDataPair;
import com.ilardi.systems.net.intalk.DebugInTalkSocketProcessor;

/**
 * @author rober
 */

@TestInstance(Lifecycle.PER_CLASS)
class CommandDataPairTest {

  private static final Logger logger = LogManager.getLogger(CommandDataPairTest.class);

  private DebugInTalkSocketProcessor intSp;

  private SessionContext context;

  @BeforeAll
  void setUp() throws Exception {
    logger.debug("setUp()");

    context = new SessionContext("TestSession", null, new SocketSession(), null, null);

    intSp = new DebugInTalkSocketProcessor();
  }

  @AfterAll
  void tearDown() throws Exception {
    logger.debug("tearDown()");

    if (context != null) {
      context.close();
    }
  }

  @Test
  void testCommandDataPair() throws IlardiNetException {
    CommandDataPair cdp1;
    byte[] bArr, bArr2, bArr3;

    logger.debug("testCommandDataPair()");

    cdp1 = new CommandDataPair();
    cdp1.setCommand(1234);
    cdp1.setDataComplete(true);
    cdp1.setMultiplexer(0);
    cdp1.setData("Hello World!".getBytes());

    bArr = cdp1.getBytes();

    for (int i = 0; i < bArr.length; i++) {
      context.enqueueData(bArr[i]);
    }

    cdp1 = new CommandDataPair();
    cdp1.setCommand(5452782);
    cdp1.setDataComplete(false);
    cdp1.setMultiplexer(678);
    cdp1.setData("Robert C. Ilardi".getBytes());
    bArr = cdp1.getBytes();

    cdp1 = new CommandDataPair();
    cdp1.setCommand(88888888);
    cdp1.setDataComplete(true);
    cdp1.setMultiplexer(88);
    cdp1.setData("Paula S. Anglo".getBytes());
    bArr2 = cdp1.getBytes();

    bArr3 = new byte[bArr.length + bArr2.length];

    System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
    System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);

    context.enqueueData(bArr3);

    intSp.process(context);

    assertTrue(true);
  }
}
