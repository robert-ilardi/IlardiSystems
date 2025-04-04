/**
 * Created Jul 11, 2016
 */
package com.ilardi.systems.metadata;

import javax.xml.bind.annotation.XmlType;

/**
 * @author Robert
 *
 */

@XmlType(name = "UserEnumerableType")
public abstract class UserEnumerableType extends MetaEntity {

  public UserEnumerableType() {
    super();
  }

}
