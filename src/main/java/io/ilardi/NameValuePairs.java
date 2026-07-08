/**
 * Created Oct 24, 2025
 */
package io.ilardi;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kate Ilardi
 */

public class NameValuePairs<T> implements Comparable<NameValuePairs<T>>, Serializable, Cloneable {

  private final Object nvpLock;
  private Map<String, NameValuePair<T>> nvMap;

  public NameValuePairs() {
    nvpLock = new Object();
    nvMap = new HashMap<>();
  }

  @Override
  public int compareTo(NameValuePairs<T> other) {
    // TODO Auto-generated method stub
    return 0;
  }

}
