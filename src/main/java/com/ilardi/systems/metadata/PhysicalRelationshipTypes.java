/**
 * Created Apr 2, 2018
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
@XmlType(name = "PhysicalRelationshipTypes")
public enum PhysicalRelationshipTypes implements Serializable {
  DICTIONARY_TO_DICTIONARY, DICTIONARY_TO_CONCEPT, CONCEPT_TO_CONCEPT, CONCEPT_TO_ATTRIBUTE, ATTRIBUTE_TO_ATTRIBUTE;
}
