/**
 * Created Aug 10, 2016
 */
package com.ilardi.experiments.dbms.query.sql;

import java.io.Serializable;

/**
 * @author rilardi
 *
 */

public enum JoinType implements Serializable {
  INNER_JOIN, OUTER_JOIN, LEFT_JOIN, RIGHT_JOIN;
}
