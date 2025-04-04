/**
 * Created Jul 12, 2016
 */
package com.ilardi.systems.metadata;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlType;

/**
 * @author Robert
 *
 */

@XmlType(name = "DictionaryEntity")
public abstract class DictionaryEntity extends MetaEntity {

  protected HashMap<MetaId, Relationship> relationshipMap;

  public DictionaryEntity() {
    super();
  }

  public HashMap<MetaId, Relationship> getRelationshipMap() {
    return relationshipMap;
  }

  public void setRelationshipMap(HashMap<MetaId, Relationship> relationshipMap) {
    this.relationshipMap = relationshipMap;
  }

  public Relationship getRelationship(MetaId mId) {
    Relationship rel = null;

    if (relationshipMap != null) {
      rel = relationshipMap.get(mId);
    }

    return rel;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((relationshipMap == null) ? 0 : relationshipMap.hashCode());
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
    if (!(obj instanceof DictionaryEntity)) {
      return false;
    }
    DictionaryEntity other = (DictionaryEntity) obj;
    if (relationshipMap == null) {
      if (other.relationshipMap != null) {
        return false;
      }
    }
    else if (!relationshipMap.equals(other.relationshipMap)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "DictionaryEntity [relationshipMap=" + relationshipMap + ", extendedProperties=" + extendedProperties + ", id=" + id + ", parentId=" + parentId + ", logicalName=" + logicalName
        + ", physicalName=" + physicalName + ", description=" + description + ", rootMetaObjectType=" + rootMetaObjectType + "]";
  }

}
