/**
 * Created Jun 29, 2016
 */
package com.ilardi.systems.metadata;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlType;

/**
 * @author rilardi
 *
 */

@XmlType(name = "MetaEntity")
public class MetaEntity extends MetaEntitySignature {

  protected HashMap<String, String> extendedProperties;

  protected MetaEntityType entityType;

  protected MetaEntityType entitySubType;

  public MetaEntity() {
    super();
  }

  public HashMap<String, String> getExtendedProperties() {
    return extendedProperties;
  }

  public void setExtendedProperties(HashMap<String, String> extendedProperties) {
    this.extendedProperties = extendedProperties;
  }

  public MetaEntityType getEntityType() {
    return entityType;
  }

  public void setEntityType(MetaEntityType entityType) {
    this.entityType = entityType;
  }

  public MetaEntityType getEntitySubType() {
    return entitySubType;
  }

  public void setEntitySubType(MetaEntityType entitySubType) {
    this.entitySubType = entitySubType;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((entitySubType == null) ? 0 : entitySubType.hashCode());
    result = prime * result + ((entityType == null) ? 0 : entityType.hashCode());
    result = prime * result + ((extendedProperties == null) ? 0 : extendedProperties.hashCode());
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
    if (!(obj instanceof MetaEntity)) {
      return false;
    }
    MetaEntity other = (MetaEntity) obj;
    if (entitySubType == null) {
      if (other.entitySubType != null) {
        return false;
      }
    }
    else if (!entitySubType.equals(other.entitySubType)) {
      return false;
    }
    if (entityType == null) {
      if (other.entityType != null) {
        return false;
      }
    }
    else if (!entityType.equals(other.entityType)) {
      return false;
    }
    if (extendedProperties == null) {
      if (other.extendedProperties != null) {
        return false;
      }
    }
    else if (!extendedProperties.equals(other.extendedProperties)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "MetaEntity [extendedProperties=" + extendedProperties + ", entityType=" + entityType + ", entitySubType=" + entitySubType + ", id=" + id + ", parentId=" + parentId + ", logicalName="
        + logicalName + ", physicalName=" + physicalName + ", description=" + description + ", entityTypeId=" + entityTypeId + ", entitySubTypeId=" + entitySubTypeId + ", rootMetaObjectType="
        + rootMetaObjectType + "]";
  }

}
