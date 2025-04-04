/**
 * Created Apr 5, 2021
 */
package com.ilardi.systems.fileheap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;

/**
 * @author robert.ilardi
 *
 */

public class FileHeap {

  private static final Logger logger = LogUtil.getInstance().getLogger(FileHeap.class);

  private static FileHeap instance = null;

  private final Object fhLock;

  private ArrayList<String> heapNames;

  private HashMap<String, String> filenameToHeapNameMap;

  private HashMap<String, MemoryManager> memManMap;

  private FileHeap() {
    fhLock = new Object();

    logger.info("File Heap Singleton Creation");

    heapNames = new ArrayList<String>();
    filenameToHeapNameMap = new HashMap<String, String>();
    memManMap = new HashMap<String, MemoryManager>();
  }

  public static synchronized FileHeap getInstance() {
    if (instance == null) {
      instance = new FileHeap();
    }

    return instance;
  }

  public void createMemoryManager(String physicalFilePath, String heapName, boolean clearHeapOnCreate) throws FileHeapException {
    synchronized (fhLock) {
      MemoryManager memMan;

      logger.info("Creating File based Memory Manager. Heap Name = " + heapName + " ; File Path = " + physicalFilePath);

      if (physicalFilePath == null) {
        throw new FileHeapException("Physical File Name Cannot be NULL");
      }

      physicalFilePath = physicalFilePath.trim();

      if (physicalFilePath.length() == 0) {
        throw new FileHeapException("Physical File Name Cannot be Empty");
      }

      if (heapName == null) {
        throw new FileHeapException("Heap Name Cannot be NULL");
      }

      heapName = heapName.trim();

      if (heapName.length() == 0) {
        throw new FileHeapException("Heap Name Cannot be Empty");
      }

      if (memManMap.containsKey(physicalFilePath)) {
        throw new FileHeapException("Memory Manager already exists using Physical File: " + physicalFilePath);
      }

      if (filenameToHeapNameMap.containsKey(physicalFilePath)) {
        throw new FileHeapException("Memory Manager already exists for Heap Name: " + heapName);
      }

      memMan = new MemoryManager(physicalFilePath, heapName, clearHeapOnCreate);

      heapNames.add(heapName);

      filenameToHeapNameMap.put(physicalFilePath, heapName);

      memManMap.put(heapName, memMan);
    }
  }

  private MemoryManager getMemoryManager(String heapName) throws FileHeapException {
    synchronized (fhLock) {
      MemoryManager memMan;

      memMan = memManMap.get(heapName);

      if (memMan == null) {
        throw new FileHeapException("File Heap Not Found for Heap Name = " + heapName);
      }

      return memMan;
    }
  }

  public void closeAll() {
    synchronized (fhLock) {
      MemoryManager memMan;
      String heapName;

      for (int i = 0; i < heapNames.size(); i++) {
        try {
          heapName = heapNames.get(i);
          memMan = memManMap.get(heapName);

          logger.info("Closing File Heap Memory Manager: " + memMan.getHeapName() + " (" + memMan.getHeapFileName() + ")");

          memMan.close();
          memMan = null;
        }
        catch (Exception e) {
          logger.error(e);
        }
      }

      heapNames.clear();
      filenameToHeapNameMap.clear();
      memManMap.clear();
    }
  }

  public void malloc(FileHeapDataStructure<? extends Serializable> dataStruct, String heapName) throws FileHeapException {
    synchronized (fhLock) {
      MemoryManager memMan;

      memMan = getMemoryManager(heapName);

      memMan.malloc(dataStruct);
    }
  }

  public void free(FileHeapDataStructure<? extends Serializable> dataStruct) throws FileHeapException {
    synchronized (fhLock) {
      MemoryManager memMan;

      memMan = dataStruct.getMemoryManager();

      memMan.free(dataStruct);
    }
  }

  public boolean variableExists(FileHeapObject<? extends Serializable> dataStruct, String heapName) throws FileHeapException {
    synchronized (fhLock) {
      MemoryManager memMan;
      boolean exists;

      memMan = getMemoryManager(heapName);

      exists = memMan.variableExists(dataStruct);

      return exists;
    }
  }

  public void remalloc(FileHeapObject<? extends Serializable> dataStruct, String heapName) throws FileHeapException {
    synchronized (fhLock) {
      MemoryManager memMan;

      memMan = getMemoryManager(heapName);

      memMan.remalloc(dataStruct);
    }
  }

  public String getHeapFileSystemInfo(String heapName) throws FileHeapException {
    synchronized (fhLock) {
      String fsInfo;
      MemoryManager memMan;

      memMan = getMemoryManager(heapName);

      fsInfo = memMan.getHeapFileSystemInfo();

      return fsInfo;
    }
  }

  public static void main(String[] args) {
    FileHeap fh = null;
    String jefsVolumeFilePath, heapName;
    int exitCd;
    FileHeapObject<String> fhoStr;
    boolean exists;
    FileHeapObject<Integer> fhoInt;

    if (args.length != 2) {
      exitCd = 1;
      System.err.println("Usage: java " + FileHeap.class.getName() + " [JEFS_VOLUME_FILE_PATH] [HEAP_NAME]");
    }
    else {
      try {
        jefsVolumeFilePath = args[0];
        jefsVolumeFilePath = jefsVolumeFilePath.trim();

        heapName = args[1];
        heapName = heapName.trim();

        fh = FileHeap.getInstance();
        fh.createMemoryManager(jefsVolumeFilePath, heapName, false);

        System.out.println(fh.getHeapFileSystemInfo(heapName));

        fhoStr = new FileHeapObject<String>();
        fhoStr.setVariableName("testString");

        if (!fh.variableExists(fhoStr, heapName)) {
          fh.malloc(fhoStr, heapName);
        }
        else {
          fh.remalloc(fhoStr, heapName);
        }

        fhoStr.updateVariableValue("Hello World!");

        System.out.println(fhoStr.retrieveVariableValue());

        System.out.println(fh.getHeapFileSystemInfo(heapName));

        fhoStr.updateVariableValue("Updated Hello World!");

        System.out.println(fhoStr.retrieveVariableValue());

        System.out.println(fh.getHeapFileSystemInfo(heapName));

        fh.free(fhoStr);

        System.out.println(fh.getHeapFileSystemInfo(heapName));

        fhoStr = new FileHeapObject<String>();
        fhoStr.setVariableName("helloWorld2");

        if (!fh.variableExists(fhoStr, heapName)) {
          fh.malloc(fhoStr, heapName);
        }
        else {
          fh.remalloc(fhoStr, heapName);
        }

        fhoStr.updateVariableValue("Hello World 2! - This is Robert C. Ilardi!!!!!!!!");

        System.out.println(fh.getHeapFileSystemInfo(heapName));

        System.out.println(fhoStr.retrieveVariableValue());

        exists = fh.variableExists(fhoStr, heapName);

        System.out.println("Variable Exists? " + (exists ? "YES" : "NO"));

        fh.free(fhoStr);

        exists = fh.variableExists(fhoStr, heapName);

        System.out.println("Variable Exists? " + (exists ? "YES" : "NO"));

        fhoInt = new FileHeapObject<Integer>();

        fhoInt.setVariableName("testInteger");

        if (!fh.variableExists(fhoInt, heapName)) {
          fh.malloc(fhoInt, heapName);
        }
        else {
          fh.remalloc(fhoInt, heapName);
        }

        fhoInt.updateVariableValue(1979);

        System.out.println(fhoInt.retrieveVariableValue());

        System.out.println(fh.getHeapFileSystemInfo(heapName));

        fh.free(fhoInt);

        System.out.println(fh.getHeapFileSystemInfo(heapName));

        exitCd = 0;
      } // End try block
      catch (Exception e) {
        exitCd = 1;
        e.printStackTrace();
      }
      finally {
        try {
          if (fh != null) {
            fh.closeAll();
            fh = null;
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
