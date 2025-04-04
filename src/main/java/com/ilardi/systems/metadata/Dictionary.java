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

@XmlType(name = "Dictionary")
public class Dictionary extends DictionaryEntity {

  protected List<Dictionary> subDictionaries;

  protected List<Concept> concepts;

  public Dictionary() {
    super();
  }

  public List<Dictionary> getSubDictionaries() {
    return subDictionaries;
  }

  public void setSubDictionaries(List<Dictionary> subDictionaries) {
    this.subDictionaries = subDictionaries;
  }

  public List<Concept> getConcepts() {
    return concepts;
  }

  public void setConcepts(List<Concept> concepts) {
    this.concepts = concepts;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((concepts == null) ? 0 : concepts.hashCode());
    result = prime * result + ((subDictionaries == null) ? 0 : subDictionaries.hashCode());
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
    if (!(obj instanceof Dictionary)) {
      return false;
    }
    Dictionary other = (Dictionary) obj;
    if (concepts == null) {
      if (other.concepts != null) {
        return false;
      }
    }
    else if (!concepts.equals(other.concepts)) {
      return false;
    }
    if (subDictionaries == null) {
      if (other.subDictionaries != null) {
        return false;
      }
    }
    else if (!subDictionaries.equals(other.subDictionaries)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Dictionary [subDictionaries=" + subDictionaries + ", concepts=" + concepts + ", relationshipMap=" + relationshipMap + ", extendedProperties=" + extendedProperties + ", id=" + id
        + ", parentId=" + parentId + ", logicalName=" + logicalName + ", physicalName=" + physicalName + ", description=" + description + ", rootMetaObjectType=" + rootMetaObjectType + "]";
  }

}
