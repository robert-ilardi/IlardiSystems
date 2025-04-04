/**
 * Created Jan 24, 2021
 */
package com.ilardi.experiments.elasticj.model;

import java.util.Properties;

/**
 * @author rilardi
 *
 */

public class EjNodeInfo extends EjModelObject {

  private Properties nodeProperties;

  private String nodeName;

  private EjNodeType nodeType;

  private String transportClassname;

  public EjNodeInfo() {
    super();
  }

  public String getNodeName() {
    return nodeName;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }

  public EjNodeType getNodeType() {
    return nodeType;
  }

  public void setNodeType(EjNodeType nodeType) {
    this.nodeType = nodeType;
  }

  public Properties getNodeProperties() {
    return nodeProperties;
  }

  public void setNodeProperties(Properties nodeProperties) {
    this.nodeProperties = nodeProperties;
  }

  public String getTransportClassname() {
    return transportClassname;
  }

  public void setTransportClassname(String transportClassname) {
    this.transportClassname = transportClassname;
  }

}
