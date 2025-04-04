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

public class Criterion extends BaseSqlElement {

  private String preBoolOp;

  private String lTableHandle;
  private String lOperand;

  private String operator;

  private int rOperandType;
  private String rTableHandle;
  private String rOperand;

  public static final char[] OPERATORS_SYMBOLS_ONLY_AND_SPACE = { ' ', '=', '!', '>', '<' };

  public static final char[] OPERATORS_SYMBOLS_ONLY = { '=', '!', '>', '<' };

  public static final int RIGHT_OPERAND_TYPE_UNKNOWN = 0;
  public static final int RIGHT_OPERAND_TYPE_STRING_LITERAL = 1;
  public static final int RIGHT_OPERAND_TYPE_INTEGER_LITERAL = 2;
  public static final int RIGHT_OPERAND_TYPE_TABLE_FIELD = 3;
  public static final int RIGHT_OPERAND_TYPE_DOUBLE_LITERAL = 4;

  public Criterion() {}

  public static Criterion Parse(String s) {
    Criterion crit = new Criterion();
    String tmp;
    String[] tmpArr;
    int pos = 0;
    char ch;

    // Do We have a boolean operator?
    tmp = RLStringUtils.GetNextWord(s, pos, OPERATORS_SYMBOLS_ONLY_AND_SPACE).trim();

    if ("AND".equalsIgnoreCase(tmp) || "OR".equalsIgnoreCase(tmp)) {
      crit.setPreBoolOp(tmp.toUpperCase());
      pos = RLStringUtils.GetPositionAfterNextWord(s, pos, OPERATORS_SYMBOLS_ONLY_AND_SPACE);
    }

    // Get Left Operand
    tmp = RLStringUtils.GetNextWord(s, pos, OPERATORS_SYMBOLS_ONLY_AND_SPACE);
    pos = RLStringUtils.GetPositionAfterNextWord(s, pos, OPERATORS_SYMBOLS_ONLY_AND_SPACE);

    if (tmp.indexOf(".") != -1) {
      tmpArr = tmp.split("\\.", 2);
      tmpArr = RLStringUtils.trimAllElements(tmpArr);

      crit.setLTableHandle(tmpArr[0]);
      crit.setLOperand(tmpArr[1]);
    }
    else {
      crit.setLOperand(tmp.trim());
    }

    // Get Operator
    tmp = RLStringUtils.GetNextWordNegativeDelimiter(s, pos, OPERATORS_SYMBOLS_ONLY);
    pos = RLStringUtils.GetPositionAfterNextWordNegativeDelimiter(s, pos, OPERATORS_SYMBOLS_ONLY);
    crit.setOperator(tmp.trim());

    // Get Right Operand
    ch = RLStringUtils.GetFirstNonWhiteSpaceChar(s, pos);

    tmp = RLStringUtils.GetNextWordRespectSingleQuotes(s, pos, '\\');
    pos = RLStringUtils.GetPositionAfterNextWordRespectSingleQuotes(s, pos, '\\');

    if (Character.isLetter(ch)) {
      crit.setROperandType(RIGHT_OPERAND_TYPE_TABLE_FIELD);

      if (tmp.indexOf(".") != -1) {
        tmpArr = tmp.split("\\.", 2);
        tmpArr = RLStringUtils.trimAllElements(tmpArr);

        crit.setRTableHandle(tmpArr[0]);
        crit.setROperand(tmpArr[1]);
      }
      else {
        crit.setROperand(tmp.trim());
      }
    }
    else {
      crit.setROperand(tmp.trim());

      if (ch == '\'') {
        crit.setROperandType(RIGHT_OPERAND_TYPE_STRING_LITERAL);
      }
      else if (RLStringUtils.IsNumeric(crit.getROperand())) {
        crit.setROperandType(RIGHT_OPERAND_TYPE_INTEGER_LITERAL);
      }
      else if (RLStringUtils.IsDouble(crit.getROperand())) {
        crit.setROperandType(RIGHT_OPERAND_TYPE_DOUBLE_LITERAL);
      }
      else {
        crit.setROperandType(RIGHT_OPERAND_TYPE_UNKNOWN);
      }
    }

    return crit;
  }

  public String getLOperand() {
    return lOperand;
  }

  public void setLOperand(String operand) {
    lOperand = operand;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public String getROperand() {
    return rOperand;
  }

  public void setROperand(String operand) {
    rOperand = operand;
  }

  public String getLTableHandle() {
    return lTableHandle;
  }

  public void setLTableHandle(String tableHandle) {
    lTableHandle = tableHandle;
  }

  public String getPreBoolOp() {
    return preBoolOp;
  }

  public void setPreBoolOp(String preBoolOp) {
    this.preBoolOp = preBoolOp;
  }

  public String getRTableHandle() {
    return rTableHandle;
  }

  public void setRTableHandle(String tableHandle) {
    rTableHandle = tableHandle;
  }

  public int getROperandType() {
    return rOperandType;
  }

  public void setROperandType(int operandType) {
    rOperandType = operandType;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();

    if (!RLStringUtils.IsNVL(preBoolOp)) {
      if ("AND".equals(preBoolOp)) {
        sb.append("&& ");
      }
      else if ("OR".equals(preBoolOp)) {
        sb.append("|| ");
      }
    }

    if (!RLStringUtils.IsNVL(lTableHandle)) {
      sb.append(lTableHandle);
      sb.append("->");
    }

    sb.append(lOperand);

    sb.append(" ");
    sb.append(operator);
    sb.append(" ");

    if (!RLStringUtils.IsNVL(rTableHandle)) {
      sb.append(rTableHandle);
      sb.append("->");
    }

    if (rOperandType == RIGHT_OPERAND_TYPE_STRING_LITERAL) {
      sb.append("\"");
      sb.append(rOperand);
      sb.append("\"");
    }
    else {
      sb.append(rOperand);
    }

    return sb.toString();
  }

}
