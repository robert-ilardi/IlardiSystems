/**
 * Created Apr 4, 2021
 */
package com.ilardi.experiments.dbms.query;

import java.io.Serializable;

/**
 * @author robert.ilardi
 *
 */

public enum DbmsDataType implements Serializable {
  DBMS_VARCHAR, DBMS_CHAR, DBMS_INTEGER, DBMS_LONG, DBMS_FLOAT, DBMS_DOUBLE, DBMS_BYTE;
}
