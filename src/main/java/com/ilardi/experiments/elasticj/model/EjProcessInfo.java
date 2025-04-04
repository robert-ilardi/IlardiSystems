/**
 * Created Jan 8, 2021
 */
package com.ilardi.experiments.elasticj.model;

/**
 * @author rilardi
 *
 */

public class EjProcessInfo extends EjModelObject {

  private int processId;

  private long startTs;

  private long exitTs;

  private int exitStatus;

  public EjProcessInfo() {
    super();
  }

}
