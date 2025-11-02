/**
 * Created Nov 2, 2025
 */
package io.ilardi;

/**
 * @Author rober
 */

public enum LastModificationChangeCodes implements DomainObject {
  INSERT('I'), UPDATE('U'), DELETE('D');

  LastModificationChangeCodes(char lstModChgCd) {
    this.lstModChgCd = lstModChgCd;
  }

  public char getLstModChgCd() {
    return lstModChgCd;
  }

  private char lstModChgCd;

  public LastModificationChangeCodes valueOf(char lstModChgCd) {
    switch (lstModChgCd) {
      case 'I':
        return INSERT;
      case 'U':
        return UPDATE;
      case 'D':
        return DELETE;
      default:
        return null;
    }
  }
}
