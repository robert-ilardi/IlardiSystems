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

@XmlType(name = "Concept")
public class Concept extends DictionaryEntity {

  protected List<Attribute> attributes;

  protected List<Concept> subConcepts;

  public Concept() {
    super();
  }

  public List<Attribute> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<Attribute> attributes) {
    this.attributes = attributes;
  }

  public List<Concept> getSubConcepts() {
    return subConcepts;
  }

  public void setSubConcepts(List<Concept> subConcepts) {
    this.subConcepts = subConcepts;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
    result = prime * result + ((subConcepts == null) ? 0 : subConcepts.hashCode());
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
    if (!(obj instanceof Concept)) {
      return false;
    }
    Concept other = (Concept) obj;
    if (attributes == null) {
      if (other.attributes != null) {
        return false;
      }
    }
    else if (!attributes.equals(other.attributes)) {
      return false;
    }
    if (subConcepts == null) {
      if (other.subConcepts != null) {
        return false;
      }
    }
    else if (!subConcepts.equals(other.subConcepts)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Concept [attributes=" + attributes + ", subConcepts=" + subConcepts + ", relationshipMap=" + relationshipMap + ", extendedProperties=" + extendedProperties + ", id=" + id + ", parentId="
        + parentId + ", logicalName=" + logicalName + ", physicalName=" + physicalName + ", description=" + description + ", rootMetaObjectType=" + rootMetaObjectType + "]";
  }

}
