/**
 * Created Sep 20, 2007
 */
package com.ilardi.experiments.dbms.query.sql.parsing;

import com.ilardi.experiments.dbms.query.sql.BaseSqlElement;
import com.roguelogic.util.RLStringUtils;

/**
 * @author Robert C. Ilardi
 *
 */

public class InputField extends BaseSqlElement {

  private String tableHandle;

  public InputField() {}

  public String getTableHandle() {
    return tableHandle;
  }

  public void setTableHandle(String tableHandle) {
    this.tableHandle = tableHandle;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();

    if (!RLStringUtils.IsNVL(tableHandle)) {
      sb.append(tableHandle);
      sb.append("->");
    }

    sb.append(name);

    return sb.toString();
  }

}
