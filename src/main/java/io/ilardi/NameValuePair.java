/**
 * Created Oct 24, 2025
 */
package io.ilardi;

import java.io.Serializable;

/**
 * @author robert.ilardi
 * @param <T>
 */

public class NameValuePair<T> implements Comparable<NameValuePair<T>>, Serializable, Cloneable {

  private String name;
  private T value;

  public NameValuePair() {
    this(null, null);
  }

  public NameValuePair(String name, T value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }

  @Override
  public int compareTo(NameValuePair<T> other) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof NameValuePair)) {
      return false;
    }
    NameValuePair<?> other = (NameValuePair<?>) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    }
    else if (!name.equals(other.name)) {
      return false;
    }
    if (value == null) {
      if (other.value != null) {
        return false;
      }
    }
    else if (!value.equals(other.value)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("NameValuePair [name=");
    builder.append(name);
    builder.append(", value=");
    builder.append(value);
    builder.append("]");
    return builder.toString();
  }

}
