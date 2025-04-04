/**
 * Created Jan 8, 2021
 */
package com.ilardi.experiments.elasticj.model;

import java.util.List;

/**
 * @author rilardi
 *
 */

public class EjProgramInfo extends EjModelObject {

  private String programName;

  private String mainClassName;

  private List<String> jarPaths;

  private String executingUserId;

  private long executionTs;

  private String executingHostAddress;

  public EjProgramInfo() {
    super();
  }

  public String getProgramName() {
    return programName;
  }

  public void setProgramName(String programName) {
    this.programName = programName;
  }

  public String getMainClassName() {
    return mainClassName;
  }

  public void setMainClassName(String mainClassName) {
    this.mainClassName = mainClassName;
  }

  public List<String> getJarPaths() {
    return jarPaths;
  }

  public void setJarPaths(List<String> jarPaths) {
    this.jarPaths = jarPaths;
  }

  public String getExecutingUserId() {
    return executingUserId;
  }

  public void setExecutingUserId(String executingUserId) {
    this.executingUserId = executingUserId;
  }

  public long getExecutionTs() {
    return executionTs;
  }

  public void setExecutionTs(long executionTs) {
    this.executionTs = executionTs;
  }

  public String getExecutingHostAddress() {
    return executingHostAddress;
  }

  public void setExecutingHostAddress(String executingHostAddress) {
    this.executingHostAddress = executingHostAddress;
  }

}
