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

public class OutputField extends BaseSqlElement {

  private String tableHandle;
  private String name;
  private String label;

  public OutputField() {}

  public static OutputField Parse(String s) {
    OutputField oField = new OutputField();
    String[] tmpArr;

    if (s.indexOf(".") != -1) {
      tmpArr = s.split("\\.", 2);
      tmpArr = RLStringUtils.trimAllElements(tmpArr);

      oField.setTableHandle(tmpArr[0]);
      oField.setName(tmpArr[1]);
    }
    else {
      oField.setName(s);
    }

    return oField;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

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

    if (!RLStringUtils.IsNVL(label)) {
      sb.append(" AS '");
      sb.append(label);
      sb.append("'");
    }

    return sb.toString();
  }

}
