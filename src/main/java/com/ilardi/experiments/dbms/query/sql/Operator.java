/**
 * Created Aug 10, 2016
 */
package com.ilardi.experiments.dbms.query.sql;

import java.io.Serializable;

/**
 * @author rilardi
 *
 */

public enum Operator implements Serializable {
  EQUAL, NOT_EQUAL, GREATER_THAN, LESS_THAN, GREATER_THAN_EQUAL_TO, LESS_THAN_EQUAL_TO, IN, LIKE, BETWEEN, NOT_IN;
}
