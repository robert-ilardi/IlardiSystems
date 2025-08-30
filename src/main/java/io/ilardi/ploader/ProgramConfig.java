/**
 * Created Jun 17, 2024
 */
package io.ilardi.ploader;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author rober
 */

public class ProgramConfig implements Serializable {

  private String friendlyName;
  private String programClassName;

  private Properties programProps;

  public ProgramConfig() {
    this(null, null, null);
  }

  public ProgramConfig(String friendlyName, String programClassName, Properties programProps) {
    this.friendlyName = friendlyName;
    this.programClassName = programClassName;
    this.programProps = programProps;
  }

  public String getFriendlyName() {
    return friendlyName;
  }

  public void setFriendlyName(String friendlyName) {
    this.friendlyName = friendlyName;
  }

  public String getProgramClassName() {
    return programClassName;
  }

  public void setProgramClassName(String programClassName) {
    this.programClassName = programClassName;
  }

  public Properties getProgramProps() {
    return programProps;
  }

  public void setProgramProps(Properties programProps) {
    this.programProps = programProps;
  }

}
