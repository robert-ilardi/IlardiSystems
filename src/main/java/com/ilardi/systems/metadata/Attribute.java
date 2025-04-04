/**
 * Apr 6, 2009
 */
package com.ilardi.systems.metadata;

import java.util.List;

import javax.xml.bind.annotation.XmlType;

/**
 * @author rilardi
 *
 */

@XmlType(name = "Attribute")
public class Attribute extends DictionaryEntity {

  protected List<Attribute> subAttributes;

  public Attribute() {
    super();
  }

  public List<Attribute> getSubAttributes() {
    return subAttributes;
  }

  public void setSubAttributes(List<Attribute> subAttributes) {
    this.subAttributes = subAttributes;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((subAttributes == null) ? 0 : subAttributes.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof Attribute)) {
      return false;
    }
    Attribute other = (Attribute) obj;
    if (subAttributes == null) {
      if (other.subAttributes != null) {
        return false;
      }
    }
    else if (!subAttributes.equals(other.subAttributes)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Attribute [subAttributes=" + subAttributes + ", relationshipMap=" + relationshipMap + ", extendedProperties=" + extendedProperties + ", id=" + id + ", parentId=" + parentId
        + ", logicalName=" + logicalName + ", physicalName=" + physicalName + ", description=" + description + ", rootMetaObjectType=" + rootMetaObjectType + "]";
  }

}
