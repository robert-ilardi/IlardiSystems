/**
 * Created Jun 30, 2016
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
@XmlType(name = "MetaIdType")
public enum MetaIdType implements Serializable {
  DICTIONARY_ID, CONCEPT_ID, ATTRIBUTE_ID, ENTITY_TYPE_ID, RELATIONSHIP_TYPE_ID;
}
