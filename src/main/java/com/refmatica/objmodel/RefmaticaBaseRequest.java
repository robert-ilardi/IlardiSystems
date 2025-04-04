/**
 * Created Sep 4, 2024
 */
package com.refmatica.objmodel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.refmatica.cmds.RefmaticaCmdArgument;
import com.refmatica.cmds.RefmaticaCmdKeyword;

/**
 * @author robert.ilardi
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("command")
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class RefmaticaBaseRequest extends RefmaticaBaseObject {

  protected RefmaticaCmdKeyword keyword;
  protected List<RefmaticaCmdArgument> arguments;

  @JsonIgnore
  protected final Object cmdLock;

  public RefmaticaBaseRequest() {
    super();
    cmdLock = new Object();
  }

  public List<RefmaticaCmdArgument> getArguments() {
    return arguments;
  }

  public void setArguments(List<RefmaticaCmdArgument> arguments) {
    this.arguments = arguments;
  }

  public Object getCmdLock() {
    return cmdLock;
  }

}
