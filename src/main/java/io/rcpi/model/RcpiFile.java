/**
 * Created Aug 27, 2024
 */
package io.rcpi.model;

import java.io.Serializable;

/**
 * @author robert.ilardi
 */

public class RcpiFile extends RcpiBinary implements Serializable, Cloneable {

  protected String absoluteFilePath;

  public RcpiFile() {}

  /**
   * @param byteArr
   */
  public RcpiFile(String absoluteFilePath, byte[] byteArr) {
    super.byteArr = byteArr;
    this.absoluteFilePath = absoluteFilePath;
  }

  /**
   * @param other
   */
  public RcpiFile(RcpiFile other) {
    copy(other);
  }

  protected void copy(RcpiFile other) {
    if (other != null) {
      this.absoluteFilePath = other.absoluteFilePath;
      super.copy(other);
    }
  }

}
