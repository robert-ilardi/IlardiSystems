/**
 * Created Mar 19, 2018
 */
package com.ilardi.systems.metadata;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * @author rilardi
 *
 */

@XmlEnum
@XmlType(name = "MetaTypeIndicator")
public enum MetaTypeIndicator implements Serializable {
  SYSTEM, USER;
}
