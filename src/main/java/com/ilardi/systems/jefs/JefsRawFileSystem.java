/**
 * Created Mar 5, 2021
 */
package com.ilardi.systems.jefs;

import static com.ilardi.systems.jefs.JefsConstants.JEFS_MAGIC_BYTES;
import static com.ilardi.systems.jefs.JefsConstants.JEFS_VERSION;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author robert.ilardi
 *
 */

public class JefsRawFileSystem extends JefsAbstractFileSystem {

  public JefsRawFileSystem(String magicBytes, int version, String physicalFilePath, String volumeName) throws IOException {
    super(magicBytes, version, physicalFilePath, volumeName);
  }

  // START Object Search Operations------>

  protected long getFirstRawFsDataPagePosition() throws IOException {
    synchronized (fsLock) {
      long objectFirstPagePosition;

      objectFirstPagePosition = getFirstRawPagePosition();

      return objectFirstPagePosition;
    }
  }

  public long getFileSystemFirstActiveDataPagePosition() throws IOException {
    return getFirstRawFsDataPagePosition();
  }

  @Override
  protected void readAllRootPagePositions(JefsPage jefsPage, ArrayList<Long> rootPagePositions) throws IOException {}

  // END Object Search Operations-------->

  public static void main(String[] args) {
    JefsRawFileSystem rawfs = null;
    String rawVolumeFilePath, objectName;
    int exitCd;
    JefsPage page;
    long pagePos;
    JefsPageHeader header;
    byte[] pageData;

    if (args.length != 1) {
      exitCd = 1;
      System.err.println("Usage: java " + JefsPageFileSystem.class.getName() + " [RAW_FS_VOLUME_FILE_PATH]");
    }
    else {
      try {
        rawVolumeFilePath = args[0];
        rawVolumeFilePath = rawVolumeFilePath.trim();

        rawfs = new JefsRawFileSystem(JEFS_MAGIC_BYTES, JEFS_VERSION, rawVolumeFilePath, null);

        // rawfs.format();

        rawfs.open();

        rawfs.printFileSystemInfo();

        rawfs.updateVolumeName("Robert's Raw File System Volume");

        objectName = LocalDateTime.now().toString();
        pageData = rawfs.generateRandomData(rawfs.getMaxPageDataLength(), false);
        page = rawfs.allocatePage(0, 0, objectName, JefsObjectType.DATAPAGE, 0, pageData);

        for (int i = 0; i < 10; i++) {
          header = page.getHeader();
          objectName = LocalDateTime.now().toString();
          pageData = rawfs.generateRandomData(rawfs.getMaxPageDataLength(), false);
          page = rawfs.allocatePage(header.getObjectId(), header.getPosition(), objectName, JefsObjectType.DATAPAGE, header.getObjectPageIndex() + 1, pageData);
        }

        rawfs.printPageChainInfo();

        System.out.println("Total Pages: " + rawfs.countPages());
        System.out.println("Active Pages: " + rawfs.countActiveDataPages());

        pagePos = rawfs.getFileSystemFirstActiveDataPagePosition();

        page = rawfs.readPageByPosition(pagePos);

        rawfs.deletePageChain(page);

        rawfs.printFileSystemInfo();

        rawfs.printPageChainInfo();

        System.out.println("Total Pages: " + rawfs.countPages());
        System.out.println("Active Pages: " + rawfs.countActiveDataPages());

        rawfs.printFreeChainInfo();

        // Finish Tests Above This Line-------------------->

        rawfs.printFileSystemInfo();

        exitCd = 0;
      } // End try block
      catch (Exception e) {
        exitCd = 1;
        e.printStackTrace();
      }
      finally {
        try {
          if (rawfs != null) {
            rawfs.close();
            rawfs = null;
          }
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    System.exit(exitCd);
  }

}
