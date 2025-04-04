/**
 * Created Mar 30, 2018
 */
package com.ilardi.systems.metadata;

import javax.xml.bind.annotation.XmlType;

/**
 * @author rilardi
 *
 */

@XmlType(name = "MetaEntitySignature")
public class MetaEntitySignature extends BaseMetaObject {

  protected MetaId id;

  protected MetaId parentId;

  protected String logicalName;

  protected String physicalName;

  protected String description;

  protected MetaId entityTypeId;

  protected MetaId entitySubTypeId;

  public MetaEntitySignature() {
    super();
  }

  public MetaId getId() {
    return id;
  }

  public void setId(MetaId id) {
    this.id = id;
  }

  public MetaId getParentId() {
    return parentId;
  }

  public void setParentId(MetaId parentId) {
    this.parentId = parentId;
  }

  public String getLogicalName() {
    return logicalName;
  }

  public void setLogicalName(String logicalName) {
    this.logicalName = logicalName;
  }

  public String getPhysicalName() {
    return physicalName;
  }

  public void setPhysicalName(String physicalName) {
    this.physicalName = physicalName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public MetaId getEntityTypeId() {
    return entityTypeId;
  }

  public void setEntityTypeId(MetaId entityTypeId) {
    this.entityTypeId = entityTypeId;
  }

  public MetaId getEntitySubTypeId() {
    return entitySubTypeId;
  }

  public void setEntitySubTypeId(MetaId entitySubTypeId) {
    this.entitySubTypeId = entitySubTypeId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((entitySubTypeId == null) ? 0 : entitySubTypeId.hashCode());
    result = prime * result + ((entityTypeId == null) ? 0 : entityTypeId.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((logicalName == null) ? 0 : logicalName.hashCode());
    result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
    result = prime * result + ((physicalName == null) ? 0 : physicalName.hashCode());
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
    if (!(obj instanceof MetaEntitySignature)) {
      return false;
    }
    MetaEntitySignature other = (MetaEntitySignature) obj;
    if (description == null) {
      if (other.description != null) {
        return false;
      }
    }
    else if (!description.equals(other.description)) {
      return false;
    }
    if (entitySubTypeId == null) {
      if (other.entitySubTypeId != null) {
        return false;
      }
    }
    else if (!entitySubTypeId.equals(other.entitySubTypeId)) {
      return false;
    }
    if (entityTypeId == null) {
      if (other.entityTypeId != null) {
        return false;
      }
    }
    else if (!entityTypeId.equals(other.entityTypeId)) {
      return false;
    }
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    }
    else if (!id.equals(other.id)) {
      return false;
    }
    if (logicalName == null) {
      if (other.logicalName != null) {
        return false;
      }
    }
    else if (!logicalName.equals(other.logicalName)) {
      return false;
    }
    if (parentId == null) {
      if (other.parentId != null) {
        return false;
      }
    }
    else if (!parentId.equals(other.parentId)) {
      return false;
    }
    if (physicalName == null) {
      if (other.physicalName != null) {
        return false;
      }
    }
    else if (!physicalName.equals(other.physicalName)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "MetaEntitySignature [id=" + id + ", parentId=" + parentId + ", logicalName=" + logicalName + ", physicalName=" + physicalName + ", description=" + description + ", entityTypeId="
        + entityTypeId + ", entitySubTypeId=" + entitySubTypeId + ", rootMetaObjectType=" + rootMetaObjectType + "]";
  }

}
