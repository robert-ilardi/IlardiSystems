/**
 * Created Sep 19, 2007
 */
package com.ilardi.experiments.dbms.query;

import java.util.ArrayList;

import com.ilardi.experiments.dbms.query.sql.BaseSqlElement;
import com.ilardi.experiments.dbms.query.sql.Keyword;
import com.ilardi.experiments.dbms.query.sql.parsing.Criterion;
import com.ilardi.experiments.dbms.query.sql.parsing.InputField;
import com.ilardi.experiments.dbms.query.sql.parsing.InputValue;
import com.ilardi.experiments.dbms.query.sql.parsing.OutputField;
import com.ilardi.experiments.dbms.query.sql.parsing.Table;
import com.roguelogic.util.RLStringUtils;

/**
 * @author Robert C. Ilardi
 *
 */

public class SqlLikeParsers {

  public static final char[] SPACE_AND_OPEN_PARENTHESIS = { ' ', '(' };

  public SqlLikeParsers() {}

  public static BaseSqlElement[] ParseQuery(String stmt) {
    BaseSqlElement[] tokens;
    ArrayList<BaseSqlElement> tList;
    char ch;
    int pos;
    StringBuffer buf;
    String tmp;
    String[] tmpArr;
    boolean foundFrom, foundWhere;
    Keyword kWord;
    OutputField oField;
    Table tab;
    Criterion crit;
    InputField iField;
    InputValue iValue;

    foundFrom = false;
    foundWhere = false;
    tList = new ArrayList<BaseSqlElement>();

    // Start with Select; skip by starting output field for loop at 7
    pos = 0;
    tmp = RLStringUtils.GetNextWord(stmt, pos);
    pos = RLStringUtils.GetPositionAfterNextWord(stmt, pos);

    kWord = new Keyword();
    kWord.setKeyword(tmp.trim().toUpperCase());
    tList.add(kWord);

    if ("UPDATE".equalsIgnoreCase(kWord.getKeyword())) {
      tmp = RLStringUtils.GetNextWord(stmt, pos);
      pos = RLStringUtils.GetPositionAfterNextWord(stmt, pos);

      if (!"SET".equalsIgnoreCase(tmp)) {
        tab = new Table();
        tab.setName(tmp.trim());
        tList.add(tab);

        pos = RLStringUtils.GetPositionAfterNextWord(stmt, pos);
      }

      // Get Output Fields
      buf = new StringBuffer();

      for (; pos < stmt.length(); pos++) {
        ch = stmt.charAt(pos);

        if (ch == ',') {
          // Found Field
          tmp = buf.toString().trim();
          tmpArr = tmp.split("=", 2);
          tmpArr = RLStringUtils.trimAllElements(tmpArr);

          iField = new InputField();
          iField.setName(tmpArr[0]);
          tList.add(iField);

          iValue = InputValue.Parse(tmpArr[1]);
          tList.add(iValue);

          buf = new StringBuffer();
        }
        else if (ch == ' ') {
          if (PeekWord("WHERE", stmt, pos)) {
            if (buf != null && buf.length() > 0) {
              tmp = buf.toString().trim();
              tmpArr = tmp.split("=", 2);
              tmpArr = RLStringUtils.trimAllElements(tmpArr);

              iField = new InputField();
              iField.setName(tmpArr[0]);
              tList.add(iField);

              iValue = InputValue.Parse(tmpArr[1]);
              tList.add(iValue);
            }

            kWord = new Keyword();
            kWord.setKeyword("WHERE");
            tList.add(kWord);

            foundWhere = true;
            buf = null;
            break;
          }
          else {
            buf.append(ch);
          }
        }
        else {
          buf.append(ch);
        }
      } // End for pos loop

      // Anything left over in the buffer? Probably corrupt QL though...
      if (buf != null && buf.length() > 0) {
        tmp = buf.toString().trim();
        tmpArr = tmp.split("=", 2);
        tmpArr = RLStringUtils.trimAllElements(tmpArr);

        iField = new InputField();
        iField.setName(tmpArr[0]);
        tList.add(iField);

        iValue = InputValue.Parse(tmpArr[1]);
        tList.add(iValue);

        buf = null;
      }
    } // End update check
    else {
      // Get Output Fields
      buf = new StringBuffer();

      for (; pos < stmt.length(); pos++) {
        ch = stmt.charAt(pos);

        if (ch == ',') {
          // Found Field
          tmp = buf.toString().trim();
          oField = OutputField.Parse(tmp);
          tList.add(oField);
          buf = new StringBuffer();
        }
        else if (ch == ' ') {
          if (PeekWord("FROM", stmt, pos)) {
            if (buf != null && buf.length() > 0) {
              tmp = buf.toString().trim();
              oField = OutputField.Parse(tmp);
              tList.add(oField);
            }

            kWord = new Keyword();
            kWord.setKeyword("FROM");
            tList.add(kWord);

            foundFrom = true;
            buf = null;
            break;
          }
          else if (PeekWord("WHERE", stmt, pos)) {
            if (buf != null && buf.length() > 0) {
              tmp = buf.toString().trim();
              oField = OutputField.Parse(tmp);
              tList.add(oField);
            }

            kWord = new Keyword();
            kWord.setKeyword("WHERE");
            tList.add(kWord);

            foundWhere = true;
            buf = null;
            break;
          }
          else {
            buf.append(ch);
          }
        }
        else {
          buf.append(ch);
        }
      } // End for pos loop

      // Anything left over in the buffer? Probably corrupt QL though...
      if (buf != null && buf.length() > 0) {
        tmp = buf.toString().trim();
        oField = OutputField.Parse(tmp);
        tList.add(oField);
        buf = null;
      }
    } // End else block for UPDATE check - This handles output fields for select
      // statements

    // Process From Clause
    if (foundFrom) {
      pos = RLStringUtils.GetPositionAfterNextWord(stmt, pos);
      buf = new StringBuffer();

      for (; pos < stmt.length(); pos++) {
        ch = stmt.charAt(pos);

        if (ch == ',') {
          // Found Table
          tmp = buf.toString().trim();
          tab = Table.Parse(tmp);
          tList.add(tab);
          buf = new StringBuffer();
        }
        else if (ch == ' ') {
          if (PeekWord("WHERE", stmt, pos)) {
            if (buf != null && buf.length() > 0) {
              tmp = buf.toString().trim();
              tab = Table.Parse(tmp);
              tList.add(tab);
            }

            kWord = new Keyword();
            kWord.setKeyword("WHERE");
            tList.add(kWord);

            foundWhere = true;
            buf = null;
            break;
          }
          else {
            buf.append(ch);
          }
        }
        else {
          buf.append(ch);
        }
      } // End for pos loop

      // Anything left over in the buffer?
      if (buf != null && buf.length() > 0) {
        tmp = buf.toString().trim();
        tab = Table.Parse(tmp);
        tList.add(tab);
        buf = null;
      }
    } // End if foundFrom

    // Process Where Clause
    if (foundWhere) {
      pos = RLStringUtils.GetPositionAfterNextWord(stmt, pos);
      buf = new StringBuffer();

      for (; pos < stmt.length(); pos++) {
        ch = stmt.charAt(pos);

        if (ch == ' ' && (PeekWord("AND", stmt, pos) || PeekWord("OR", stmt, pos))) {
          // Found Criterion
          tmp = buf.toString().trim();
          crit = Criterion.Parse(tmp);
          tList.add(crit);
          buf = new StringBuffer();
        }
        else {
          buf.append(ch);
        }
      } // End for pos loop

      // Anything left over in the buffer?
      if (buf != null && buf.length() > 0) {
        tmp = buf.toString().trim();
        crit = Criterion.Parse(tmp);
        tList.add(crit);
        buf = null;
      }
    } // End if foundWhere

    tokens = new BaseSqlElement[tList.size()];
    tokens = tList.toArray(tokens);

    return tokens;
  }

  public static boolean PeekWord(String word, String stmt, int pos) {
    boolean isNext = false;
    String nextWord;

    nextWord = RLStringUtils.GetNextWord(stmt, pos);
    isNext = nextWord.equalsIgnoreCase(word);

    return isNext;
  }

  public static BaseSqlElement[] ParseInsert(String stmt) {
    BaseSqlElement[] tokens;
    ArrayList<BaseSqlElement> tList;
    char ch;
    int pos;
    StringBuffer buf;
    String tmp;
    Keyword kWord;
    Table tab;
    InputField iField;
    InputValue iValue;

    tList = new ArrayList<BaseSqlElement>();

    // Start with Select; skip by starting output field for loop at 7
    pos = 7;

    kWord = new Keyword();
    kWord.setKeyword("INSERT");
    tList.add(kWord);

    // Get Table
    buf = new StringBuffer();

    if (PeekWord("INTO", stmt, pos)) {
      pos = RLStringUtils.GetPositionAfterNextWord(stmt, pos);

      tmp = RLStringUtils.GetNextWord(stmt, pos, SPACE_AND_OPEN_PARENTHESIS);
      tab = new Table();
      tab.setName(tmp.trim());

      pos = RLStringUtils.GetPositionAfterNextWord(stmt, pos, SPACE_AND_OPEN_PARENTHESIS);

      buf = null;
    }

    // Get Input Fields
    buf = new StringBuffer();
    pos = stmt.indexOf("(", pos) + 1;

    for (; pos < stmt.length(); pos++) {
      ch = stmt.charAt(pos);

      if (ch == ',') {
        // Found Field
        tmp = buf.toString().trim();
        iField = new InputField();
        iField.setName(tmp);
        tList.add(iField);
        buf = new StringBuffer();
      }
      else if (ch == ')') {
        break;
      }
      else {
        buf.append(ch);
      }
    } // End for pos loop

    // Anything left over in the buffer? Probably corrupt QL though...
    if (buf != null && buf.length() > 0) {
      tmp = buf.toString().trim();
      iField = new InputField();
      iField.setName(tmp);
      tList.add(iField);
      buf = null;
    }

    // Get Input Values
    buf = new StringBuffer();
    pos = stmt.indexOf("(", pos) + 1;

    for (; pos < stmt.length(); pos++) {
      ch = stmt.charAt(pos);

      if (ch == ',') {
        // Found Field
        tmp = buf.toString().trim();
        iValue = InputValue.Parse(tmp);
        tList.add(iValue);
        buf = new StringBuffer();
      }
      else if (ch == ')') {
        break;
      }
      else {
        buf.append(ch);
      }
    } // End for pos loop

    // Anything left over in the buffer? Probably corrupt QL though...
    if (buf != null && buf.length() > 0) {
      tmp = buf.toString().trim();
      iValue = InputValue.Parse(tmp);
      tList.add(iValue);
      buf = null;
    }

    tokens = new BaseSqlElement[tList.size()];
    tokens = tList.toArray(tokens);

    return tokens;
  }

}
