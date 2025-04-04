/**
 * Created Apr 5, 2021
 */
package com.ilardi.systems.fileheap;

import java.io.Serializable;

/**
 * @author robert.ilardi
 *
 */

public class FileHeapList<T extends Serializable> extends FileHeapBaseDataStructure<T> {

  public FileHeapList() {
    super();
  }

  @Override
  public void updateVariableValue(T value) throws FileHeapException {
    // TODO Auto-generated method stub

  }

  @Override
  public T retrieveVariableValue() throws FileHeapException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public VariableHeader getVariableHeader(T value) throws FileHeapException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public byte[] getVariableValueAsBytes(T value, VariableHeader varHeader) throws FileHeapException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public T getVariableValue(VariableHeader varHeader) throws FileHeapException {
    // TODO Auto-generated method stub
    return null;
  }

}
