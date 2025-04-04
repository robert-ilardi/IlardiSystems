/**
 * Created Aug 27, 2024
 */
package io.rcpi.model;

import java.io.Serializable;

/**
 * @author robert.ilardi
 */

public class RcpiBinary implements Serializable {

  protected byte[] byteArr;

  public RcpiBinary() {}

  public RcpiBinary(byte[] byteArr) {
    this.byteArr = byteArr;
  }

  public RcpiBinary(RcpiBinary other) {
    copy(other);
  }

  protected void copy(RcpiBinary other) {
    if (other != null && other.byteArr != null) {
      this.byteArr = new byte[other.byteArr.length];
      System.arraycopy(other.byteArr, 0, this.byteArr, 0, this.byteArr.length);
    }
  }

  public byte[] getAsByteArray() {
    return byteArr;
  }

  public void toRcpiBinary(byte[] byteArr) {
    this.byteArr = byteArr;
  }

}
