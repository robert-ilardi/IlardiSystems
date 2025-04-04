/**
 * Created Apr 5, 2021
 */
package com.ilardi.systems.fileheap;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.List;

import com.ilardi.experiments.log.LogUtil;
import com.ilardi.experiments.log.Logger;
import com.ilardi.systems.jefs.JefsHeapFileSystem;
import com.ilardi.systems.jefs.JefsObjectType;
import com.ilardi.systems.jefs.JefsPage;
import com.ilardi.systems.jefs.JefsPageHeader;

/**
 * @author robert.ilardi
 *
 */

public class MemoryManager {

  private static final Logger logger = LogUtil.getInstance().getLogger(MemoryManager.class);

  private final Object memManLock;

  private JefsHeapFileSystem jefs;

  public MemoryManager(String physicalFilePath, String heapName, boolean clearHeapOnCreate) throws FileHeapException {
    try {
      memManLock = new Object();

      logger.info("Initializing File Heap Memory Manager. Heap Name = " + heapName + " ; File Path = " + physicalFilePath);

      jefs = new JefsHeapFileSystem(physicalFilePath, heapName);

      if (clearHeapOnCreate) {
        jefs.format();
      }

      jefs.open();

      logger.info(jefs.getFileSystemInfoString());
    }
    catch (Exception e) {
      throw new FileHeapException("An error occurred while attempting to Initialize JEFS File System. System Message: " + e.getMessage(), e);
    }
  }

  public String getHeapName() {
    synchronized (memManLock) {
      return jefs.getVolumeName();
    }
  }

  public String getHeapFileName() {
    synchronized (memManLock) {
      return jefs.getPhysicalFileName();
    }
  }

  public void close() throws FileHeapException {
    synchronized (memManLock) {
      try {
        if (jefs != null) {
          jefs.close();
          jefs = null;
        }
      }
      catch (Exception e) {
        throw new FileHeapException("An error occurred while attempting to close JEFS File System. System Message: " + e.getMessage(), e);
      }
    }
  }

  public void malloc(FileHeapDataStructure<? extends Serializable> dataStruct) throws FileHeapException {
    synchronized (memManLock) {
      String varName;
      boolean exists;

      if (dataStruct == null) {
        throw new FileHeapException("Data Structure CANNOT be NULL");
      }

      varName = dataStruct.getVariableName();

      if (varName == null) {
        throw new FileHeapException("Variable Name CANNOT be NULL");
      }

      varName = varName.trim();

      if (varName.length() == 0) {
        throw new FileHeapException("Variable Name CANNOT be Empty");
      }

      try {
        exists = jefs.containsRootPagesNamed(varName);

        if (exists) {
          throw new FileHeapException("Variable '" + varName + "' Already Exists in Heap '" + getHeapName() + "'");
        }

        addMemoryManagementEntry(dataStruct);

        dataStruct.setMemoryManager(this);
      }
      catch (Exception e) {
        throw new FileHeapException("An error occurred while attempting Perform MALLOC. System Message: " + e.getMessage(), e);
      }
    }
  }

  public void remalloc(FileHeapDataStructure<? extends Serializable> dataStruct) throws FileHeapException {
    synchronized (memManLock) {
      String varName;
      boolean exists;
      List<JefsPageHeader> jefsHeaders;
      JefsPageHeader jefsHeader;

      if (dataStruct == null) {
        throw new FileHeapException("Data Structure CANNOT be NULL");
      }

      varName = dataStruct.getVariableName();

      if (varName == null) {
        throw new FileHeapException("Variable Name CANNOT be NULL");
      }

      varName = varName.trim();

      if (varName.length() == 0) {
        throw new FileHeapException("Variable Name CANNOT be Empty");
      }

      try {
        exists = jefs.containsRootPagesNamed(varName);

        if (!exists) {
          throw new FileHeapException("Variable '" + varName + "' Does NOT Exist in Heap '" + getHeapName() + "'");
        }

        dataStruct.setMemoryManager(this);

        jefsHeaders = jefs.findRootPagesByObjectName(varName);

        if (jefsHeaders == null || jefsHeaders.isEmpty()) {
          throw new FileHeapException("Variable '" + varName + "' Not Found");
        }

        if (jefsHeaders.size() > 1) {
          throw new FileHeapException("Multiple Entries for Variable '" + varName + "' Found! Heap is Corrupt!");
        }

        jefsHeader = jefsHeaders.get(0);
        dataStruct.setJefsHeader(jefsHeader);
      }
      catch (Exception e) {
        throw new FileHeapException("An error occurred while attempting Perform REMALLOC. System Message: " + e.getMessage(), e);
      }
    }
  }

  public void free(FileHeapDataStructure<? extends Serializable> dataStruct) throws FileHeapException {
    synchronized (memManLock) {
      if (dataStruct == null) {
        throw new FileHeapException("Data Structure CANNOT be NULL");
      }

      deleteVariableValue(dataStruct);

      dataStruct.setMemoryManager(null);
      dataStruct.setJefsHeader(null);
    }
  }

  public void updateVariableValue(String variableName, JefsPageHeader jefsHeader, byte[] variableHeader, byte[] variableValue) throws FileHeapException {
    synchronized (memManLock) {
      ByteArrayOutputStream baos = null;
      byte[] data;
      // List<JefsPageHeader> headers;
      JefsPage page;

      if (variableName == null) {
        throw new FileHeapException("Variable Name CANNOT be NULL");
      }

      if (variableName.length() == 0) {
        throw new FileHeapException("Variable Name CANNOT be Empty");
      }

      if (jefsHeader == null) {
        throw new FileHeapException("JEFS Page Header CANNOT be NULL");
      }

      if (variableHeader == null) {
        throw new FileHeapException("Variable Header CANNOT be NULL");
      }

      if (variableHeader.length == 0) {
        throw new FileHeapException("Variable Header CANNOT be Empty");
      }

      try {
        baos = new ByteArrayOutputStream(variableHeader.length + variableValue.length);

        baos.write(variableHeader);
        baos.write(variableValue);

        data = baos.toByteArray();

        /*
         * headers = jefs.findRootPagesByObjectName(variableName);
         * 
         * if (headers == null || headers.isEmpty()) { throw new
         * FileHeapException("Variable '" + variableName + "' Not Found"); }
         * 
         * if (headers.size() > 1) { throw new
         * FileHeapException("Multiple Entries for Variable '" + variableName +
         * "' Found! Heap is Corrupt!"); }
         * 
         * jefsHeader = headers.get(0);
         */

        page = jefs.readPage(jefsHeader);

        if (page != null && page.getUsedDataSize() > 0) {
          jefs.truncatePageChainData(page);
          page = jefs.readPage(jefsHeader);
        }

        jefs.appendDataToPageChain(page, data);
      }
      catch (Exception e) {
        throw new FileHeapException("An error occurred while attempting to Update Variable Value. System Message: " + e.getMessage(), e);
      }
      finally {
        try {
          if (baos != null) {
            baos.close();
            baos = null;
          }
        }
        catch (Exception e) {
          logger.error(e);
        }
      }
    }
  }

  public VariableHeader retrieveVariableHeader(FileHeapDataStructure<? extends Serializable> dataStruct) throws FileHeapException {
    synchronized (memManLock) {
      VariableHeader varHeader;
      String variableName;
      List<JefsPageHeader> jefsHeaders;
      JefsPageHeader jefsHeader;
      JefsPage jefsPage;
      byte[] varHeaderBytes, bTmp;
      ByteArrayOutputStream baos = null;
      int varHeaderLen, remainingLen;
      long nextPagePos;
      ByteBuffer buf;

      if (dataStruct == null) {
        throw new FileHeapException("Data Structure CANNOT be NULL");
      }

      try {
        variableName = dataStruct.getVariableName();

        jefsHeader = dataStruct.getJefsHeader();

        if (jefsHeader == null) {
          jefsHeaders = jefs.findRootPagesByObjectName(variableName);

          if (jefsHeaders == null || jefsHeaders.isEmpty()) {
            throw new FileHeapException("Variable '" + variableName + "' Not Found");
          }

          if (jefsHeaders.size() > 1) {
            throw new FileHeapException("Multiple Entries for Variable '" + variableName + "' Found! Heap is Corrupt!");
          }

          jefsHeader = jefsHeaders.get(0);
        }

        jefsPage = jefs.readPage(jefsHeader);

        bTmp = jefsPage.getUsedDataOnly();
        buf = ByteBuffer.wrap(bTmp);
        varHeaderLen = buf.getInt();

        if (bTmp.length < varHeaderLen) {
          baos = new ByteArrayOutputStream();
          baos.write(bTmp);

          remainingLen = varHeaderLen - bTmp.length;

          nextPagePos = jefsPage.getNextPagePosition();

          while (remainingLen > 0 && nextPagePos > 0) {
            jefsPage = jefs.readPageByPosition(nextPagePos);

            bTmp = jefsPage.getUsedDataOnly();

            if (remainingLen > bTmp.length) {
              baos.write(bTmp);
            }
            else {
              baos.write(bTmp, 0, remainingLen);
            }

            remainingLen -= bTmp.length;

            nextPagePos = jefsPage.getNextPagePosition();
          }

          bTmp = baos.toByteArray();
        }

        varHeaderBytes = new byte[varHeaderLen];
        System.arraycopy(bTmp, 0, varHeaderBytes, 0, varHeaderLen);

        varHeader = new VariableHeader(varHeaderBytes);
        varHeader.setJefsHeader(jefsHeader);

        return varHeader;
      }
      catch (Exception e) {
        throw new FileHeapException("An error occurred while attempting to Retrieve Variable Header. System Message: " + e.getMessage(), e);
      }
      finally {
        try {
          if (baos != null) {
            baos.close();
            baos = null;
          }
        }
        catch (Exception e) {
          logger.error(e);
        }
      }
    }
  }

  public byte[] retrieveVariableData(FileHeapDataStructure<? extends Serializable> dataStruct, VariableHeader varHeader) throws FileHeapException {
    synchronized (memManLock) {
      JefsPageHeader jefsHeader;
      byte[] varValBytes, bTmp;
      JefsPage jefsPage;
      ByteArrayOutputStream baos = null;
      int varValLen, remainingLen;
      int varHeaderLen, firstPageVarValLen;
      long nextPagePos;

      if (dataStruct == null) {
        throw new FileHeapException("Data Structure CANNOT be NULL");
      }

      if (varHeader == null) {
        throw new FileHeapException("Variable Header CANNOT be NULL");
      }

      jefsHeader = varHeader.getJefsHeader();

      if (jefsHeader == null) {
        throw new FileHeapException("JEFS Page Header CANNOT be NULL");
      }

      try {
        varValLen = varHeader.sumDataElementLengths();
        varHeaderLen = varHeader.getVarHeaderLen();
        jefsPage = jefs.readPage(jefsHeader);

        bTmp = jefsPage.getUsedDataOnly();

        firstPageVarValLen = bTmp.length - varHeaderLen;

        baos = new ByteArrayOutputStream();
        baos.write(bTmp, varHeaderLen, firstPageVarValLen);

        remainingLen = varValLen - firstPageVarValLen;

        nextPagePos = jefsPage.getNextPagePosition();

        while (remainingLen > 0 && nextPagePos > 0) {
          jefsPage = jefs.readPageByPosition(nextPagePos);

          bTmp = jefsPage.getUsedDataOnly();

          if (remainingLen > bTmp.length) {
            baos.write(bTmp);
          }
          else {
            baos.write(bTmp, 0, remainingLen);
          }

          remainingLen -= bTmp.length;

          nextPagePos = jefsPage.getNextPagePosition();
        }

        bTmp = baos.toByteArray();

        varValBytes = new byte[varValLen];
        System.arraycopy(bTmp, 0, varValBytes, 0, varValLen);

        return varValBytes;
      }
      catch (Exception e) {
        throw new FileHeapException("An error occurred while attempting to Retrieve Variable Data. System Message: " + e.getMessage(), e);
      }
      finally {
        try {
          if (baos != null) {
            baos.close();
            baos = null;
          }
        }
        catch (Exception e) {
          logger.error(e);
        }
      }
    }

  }

  private void addMemoryManagementEntry(FileHeapDataStructure<? extends Serializable> dataStruct) throws FileHeapException {
    synchronized (memManLock) {
      JefsPage page;
      JefsPageHeader header;

      if (dataStruct == null) {
        throw new FileHeapException("Data Structure CANNOT be NULL");
      }

      try {
        page = jefs.allocatePage(0, 0, dataStruct.getVariableName(), JefsObjectType.VARIABLE, 0, null);
        header = page.getHeader();
        dataStruct.setJefsHeader(header);
      }
      catch (Exception e) {
        throw new FileHeapException("An error occurred while attempting to Add Memory Management Entry. System Message: " + e.getMessage(), e);
      }
    }
  }

  private void deleteVariableValue(FileHeapDataStructure<? extends Serializable> dataStruct) throws FileHeapException {
    synchronized (memManLock) {
      List<JefsPageHeader> jefsHeaders;
      JefsPageHeader jefsHeader;
      JefsPage jefsPage;
      String variableName;

      if (dataStruct == null) {
        throw new FileHeapException("Data Structure CANNOT be NULL");
      }

      try {
        variableName = dataStruct.getVariableName();

        jefsHeader = dataStruct.getJefsHeader();

        if (jefsHeader == null) {
          jefsHeaders = jefs.findRootPagesByObjectName(variableName);

          if (jefsHeaders == null || jefsHeaders.isEmpty()) {
            throw new FileHeapException("Variable '" + variableName + "' Not Found");
          }

          if (jefsHeaders.size() > 1) {
            throw new FileHeapException("Multiple Entries for Variable '" + variableName + "' Found! Heap is Corrupt!");
          }

          jefsHeader = jefsHeaders.get(0);
        }

        jefsPage = jefs.readPage(jefsHeader);

        jefs.deletePageChain(jefsPage);
      }
      catch (Exception e) {
        throw new FileHeapException("An error occurred while attempting to Delete Variable Value. System Message: " + e.getMessage(), e);
      }
    }
  }

  public boolean variableExists(FileHeapObject<? extends Serializable> dataStruct) throws FileHeapException {
    synchronized (memManLock) {
      boolean exists;
      String varName;

      if (dataStruct == null) {
        throw new FileHeapException("Data Structure CANNOT be NULL");
      }

      varName = dataStruct.getVariableName();

      if (varName == null) {
        throw new FileHeapException("Variable Name CANNOT be NULL");
      }

      varName = varName.trim();

      if (varName.length() == 0) {
        throw new FileHeapException("Variable Name CANNOT be Empty");
      }

      try {
        exists = jefs.containsRootPagesNamed(varName);

        return exists;
      }
      catch (Exception e) {
        throw new FileHeapException("An error occurred while attempting to Check Variable Exists. System Message: " + e.getMessage(), e);
      }
    }
  }

  public String getHeapFileSystemInfo() {
    synchronized (memManLock) {
      String fsInfo;

      fsInfo = jefs.getFileSystemInfoString();

      return fsInfo;
    }
  }

}
