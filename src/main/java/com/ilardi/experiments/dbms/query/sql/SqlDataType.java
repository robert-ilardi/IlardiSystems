/**
 * Created Aug 10, 2016
 */
package com.ilardi.experiments.dbms.query.sql;

import java.io.Serializable;

/**
 * @author rilardi
 *
 */

public enum SqlDataType implements Serializable {
  NULL_VALUE, VARCHAR, CHAR, STRING, INTEGER, FLOAT, STRING_IN_LIST, INTEGER_IN_LIST, GENERIC_IN_LIST, SUB_QUERY_RESULT, FUNCTION_RESULT, DATE, TIME, DATETIME, CLOB;
}
