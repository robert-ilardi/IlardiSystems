/**
 * Created Mar 30, 2018
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
@XmlType(name = "RootMetaObjectTypes")
public enum RootMetaObjectTypes implements Serializable {
  DICTIONARY, CONCEPT, ATTRIBUTE, RELATIONSHIP, META_ID, META_ENTITY_TYPE, RELATIONSHIP_TYPE;
}
