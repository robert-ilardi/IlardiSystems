/**
 * Created Aug 10, 2016
 */
package com.ilardi.experiments.dbms.query.sql;

import java.io.Serializable;

/**
 * @author rilardi
 *
 */

public enum SqlStatement implements Serializable {
  SELECT, INSERT, UPDATE, DELETE, CREATE_TABLE, DROP_TABLE;
}
