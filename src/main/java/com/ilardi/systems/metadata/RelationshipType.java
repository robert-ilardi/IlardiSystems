/**
 * Apr 6, 2009
 */
package com.ilardi.systems.metadata;

import javax.xml.bind.annotation.XmlType;

/**
 * @author rilardi
 *
 */

@XmlType(name = "RelationshipType")
public class RelationshipType extends UserEnumerableType {

  protected MetaTypeIndicator typeIndicator;

  public RelationshipType() {
    super();
  }

  public MetaTypeIndicator getTypeIndicator() {
    return typeIndicator;
  }

  public void setTypeIndicator(MetaTypeIndicator typeIndicator) {
    this.typeIndicator = typeIndicator;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((typeIndicator == null) ? 0 : typeIndicator.hashCode());
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
    if (!(obj instanceof RelationshipType)) {
      return false;
    }
    RelationshipType other = (RelationshipType) obj;
    if (typeIndicator != other.typeIndicator) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "RelationshipType [typeIndicator=" + typeIndicator + ", extendedProperties=" + extendedProperties + ", id=" + id + ", parentId=" + parentId + ", logicalName=" + logicalName
        + ", physicalName=" + physicalName + ", description=" + description + ", rootMetaObjectType=" + rootMetaObjectType + "]";
  }

}
