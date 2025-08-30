/**
 * Created Jul 28, 2021
 */
package com.ilardi.systems.jefs;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import io.ilardi.ApplicationContext;

/**
 * @author robert.ilardi
 *
 */

@TestInstance(Lifecycle.PER_CLASS)
class JefsFileSystemTest {

  private static final Logger logger = LogManager.getLogger(JefsFileSystemTest.class);

  private JefsFileSystem jefs;
  private String jefsVolumeFilePath;
  private ApplicationContext appContext;

  @BeforeAll
  void setUp() throws Exception {
    logger.debug("setUp()");

    appContext = ApplicationContext.getInstance();

    jefsVolumeFilePath = appContext.getTempDir();
    jefsVolumeFilePath += "/test-iostream.jefs";

    logger.debug("JEFS Volume File Path = " + jefsVolumeFilePath);

    jefs = new JefsFileSystem(jefsVolumeFilePath, "IO Stream Test Volume");
    jefs.format();
    jefs.open();
    jefs.printFileSystemInfo();
  }

  @AfterAll
  void tearDown() throws Exception {
    logger.debug("tearDown()");

    if (jefs != null) {
      jefs.close();
      jefs = null;
    }
  }

  @Test
  void testJefsInputOutputStream() throws Exception {
    JefsFile jefsFile = null;
    OutputStream outs = null;
    JefsRandomAccessFile raf = null;
    byte[] buf;
    long sz;

    try {
      logger.debug("testJefsInputOutputStream()");

      jefs.mkdir("/tests/");
      jefsFile = jefs.openFile("/tests/data.txt");

      logger.debug(jefsFile);

      outs = jefsFile.openOutputStream(false);
      logger.debug(outs);

      outs.write("Robert C. Ilardi".getBytes());

      outs.write("\nPaula S. Anglo\n".getBytes());

      outs.flush();

      buf = jefs.generateRandomText(16384, true);
      outs.write(buf);

      outs.write("\nLily was the Best Husky Girl Ever!\n".getBytes());
      outs.write("Thor is the Best Husky Boy Ever!\n".getBytes());

      outs.flush();

      outs.close();
      outs = null;

      outs = jefsFile.openOutputStream(true);
      logger.debug(outs);

      outs.write(">>>>>>>>>>>>> Testing Append Mode!\n".getBytes());

      outs.close();
      outs = null;

      readJefsFile(jefsFile);

      logger.debug(">>>>>>>>>>>>> Testing Random Access File!");

      raf = jefsFile.openRandomAccessFile();

      sz = raf.size();

      raf.seek(sz - 1);

      raf.write("\n$Robert Is the Best!".getBytes());

      raf.seek(16416);

      readJefsFile(raf);

      outs = jefsFile.openOutputStream(false);
      logger.debug(outs);

      outs.write("************************ Testing File Overwrite!\n".getBytes());

      outs.close();
      outs = null;

      readJefsFile(jefsFile);

      assertTrue(true);
    }
    finally {
      if (outs != null) {
        try {
          outs.close();
          outs = null;
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }

      if (raf != null) {
        try {
          raf.close();
          raf = null;
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void readJefsFile(JefsRandomAccessFile raf) throws IOException {
    int len;
    byte[] buf;
    String tmpStr;

    logger.debug(raf);

    buf = new byte[256];
    len = raf.read(buf);

    while (len != -1) {
      tmpStr = new String(buf, 0, len);

      logger.debug(tmpStr);

      len = raf.read(buf);
    }
  }

  private void readJefsFile(JefsFile jefsFile) throws IOException {
    InputStream ins = null;
    int len;
    byte[] buf;
    String tmpStr;

    try {
      ins = jefsFile.openInputStream();
      logger.debug(ins);

      buf = new byte[256];
      len = ins.read(buf);

      while (len != -1) {
        tmpStr = new String(buf, 0, len);

        logger.debug(tmpStr);

        len = ins.read(buf);
      }
    }
    finally {
      if (ins != null) {
        try {
          ins.close();
          ins = null;
        }
        catch (Exception e) {

        }
      }
    }
  }

}
