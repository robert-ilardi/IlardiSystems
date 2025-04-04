/**
 * Created Aug 26, 2024
 */
package io.rcpi.model;

import java.io.Serializable;

/**
 * @author robert.ilardi
 */

public class RcpiCmdArg implements Serializable {

  private String name;
  private RcpiCmdArgValue value;

  private String customTypeName; // Used if dataType is OBJECT
  private RcpiCmdArgDataType dataType;
  private RcpiCmdArgAllocationType allocationType;

  public RcpiCmdArg() {}

}
