/**
 * Created Apr 5, 2021
 */
package com.ilardi.systems.fileheap;

import java.io.Serializable;

import com.ilardi.systems.jefs.JefsPageHeader;

/**
 * @author robert.ilardi
 *
 */

public interface FileHeapDataStructure<T extends Serializable> {

  public void setMemoryManager(MemoryManager memMan);

  public MemoryManager getMemoryManager();

  public void setVariableName(String variableName);

  public String getVariableName();

  public void updateVariableValue(T value) throws FileHeapException;

  public T retrieveVariableValue() throws FileHeapException;

  public VariableHeader getVariableHeader(T value) throws FileHeapException;

  public byte[] getVariableValueAsBytes(T value, VariableHeader varHeader) throws FileHeapException;

  public T getVariableValue(VariableHeader varHeader) throws FileHeapException;

  public void setJefsHeader(JefsPageHeader jefsHeader);

  public JefsPageHeader getJefsHeader();

}
