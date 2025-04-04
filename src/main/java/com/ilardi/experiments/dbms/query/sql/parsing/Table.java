/**
 * Created Sep 19, 2007
 */
package com.ilardi.experiments.dbms.query.sql.parsing;

import com.ilardi.experiments.dbms.query.sql.BaseSqlElement;
import com.roguelogic.util.RLStringUtils;

/**
 * @author Robert C. Ilardi
 *
 */

public class Table extends BaseSqlElement {

  private String alias;

  public Table() {}

  public static Table Parse(String s) {
    Table tab = new Table();
    String[] tmpArr;

    if (s.indexOf(" ") != -1) {
      tmpArr = s.split(" ", 2);
      tmpArr = RLStringUtils.trimAllElements(tmpArr);

      tab.setName(tmpArr[0]);
      tab.setAlias(tmpArr[1]);
    }
    else {
      tab.setName(s);
    }

    return tab;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();

    sb.append(name);

    if (!RLStringUtils.IsNVL(alias)) {
      sb.append("(");
      sb.append(alias);
      sb.append(")");
    }

    return sb.toString();
  }

}
