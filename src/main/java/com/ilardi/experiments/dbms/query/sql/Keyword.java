/**
 * Created Sep 19, 2007
 */
package com.ilardi.experiments.dbms.query.sql;

/**
 * @author Robert C. Ilardi
 *
 */

public class Keyword extends BaseSqlElement {

  private String keyword;

  public Keyword() {}

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String toString() {
    return keyword;
  }

}
