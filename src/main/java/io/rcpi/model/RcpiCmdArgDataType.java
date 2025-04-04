/**
 * Created Aug 26, 2024
 */
package io.rcpi.model;

import java.io.Serializable;

/**
 * @author robert.ilardi
 */

public enum RcpiCmdArgDataType implements Serializable {
  SHORT, // 16-bit integer
  INTEGER, // 32-bit integer
  LONG, // 64-bit integer
  FLOAT, // 32-bit floating-point
  DOUBLE, // 64-bit floating-point
  STRING, // Text data
  CHARACTER, // Single character
  BYTE, // Single byte or byte array depending on allocationType
  BINARY, // Variable-length binary data
  BOOLEAN, // Boolean true/false
  DATE, // Calendar date (YYYY-MM-DD)
  TIME, // Time of day (HH:mm:ss or with milliseconds: HH:mm:ss.SSS)
  DATETIME, // Combined date and time with timezone support (ISO 8601: YYYY-MM-DDTHH:mm:ssZ
            // or with milliseconds: YYYY-MM-DDTHH:mm:ss.SSSZ)
  OBJECT // Custom or complex data types
}
