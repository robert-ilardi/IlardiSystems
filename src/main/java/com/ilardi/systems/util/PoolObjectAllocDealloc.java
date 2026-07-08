/**
 * Created Feb 25, 2024
 */
package com.ilardi.systems.util;

import io.ilardi.IlardiSystemsException;

/**
 * @author Kate Ilardi
 *
 */

public interface PoolObjectAllocDealloc<T> {

  public T allocate() throws IlardiSystemsException;

  public void deallocate(T obj) throws IlardiSystemsException;

}
