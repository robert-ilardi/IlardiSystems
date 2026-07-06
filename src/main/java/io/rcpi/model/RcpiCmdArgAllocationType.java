/**
 * Created Aug 26, 2024
 */
package io.rcpi.model;

import java.io.Serializable;

/**
 * @author kilardi
 */

public enum RcpiCmdArgAllocationType implements Serializable {
  SCALAR, // Single value
  ARRAY, // Fixed-size array
  LIST // Dynamic list
}
