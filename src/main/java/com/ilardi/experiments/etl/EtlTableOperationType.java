/**
 * Created Jun 20, 2017
 */
package com.ilardi.experiments.etl;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * @author rilardi
 *
 */

@XmlEnum
@XmlType(name = "EtlTableOperationType")
public enum EtlTableOperationType implements Serializable {
  CREATE, DROP, READ, INSERT, UPDATE, DELETE;
}
