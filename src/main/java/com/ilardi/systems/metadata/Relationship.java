/**
 * Apr 6, 2009
 */
package com.ilardi.systems.metadata;

import javax.xml.bind.annotation.XmlType;

/**
 * @author rilardi
 *
 */

@XmlType(name = "Relationship")
public class Relationship extends BaseMetaObject {

  private MetaId thisId;

  private MetaId relatedId;

  private RelationshipType relationshipType;

  public Relationship() {
    super();
  }

  public MetaId getThisId() {
    return thisId;
  }

  public void setThisId(MetaId thisId) {
    this.thisId = thisId;
  }

  public MetaId getRelatedId() {
    return relatedId;
  }

  public void setRelatedId(MetaId relatedId) {
    this.relatedId = relatedId;
  }

  public RelationshipType getRelationshipType() {
    return relationshipType;
  }

  public void setRelationshipType(RelationshipType relationshipType) {
    this.relationshipType = relationshipType;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((relatedId == null) ? 0 : relatedId.hashCode());
    result = prime * result + ((relationshipType == null) ? 0 : relationshipType.hashCode());
    result = prime * result + ((thisId == null) ? 0 : thisId.hashCode());
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
    if (!(obj instanceof Relationship)) {
      return false;
    }
    Relationship other = (Relationship) obj;
    if (relatedId == null) {
      if (other.relatedId != null) {
        return false;
      }
    }
    else if (!relatedId.equals(other.relatedId)) {
      return false;
    }
    if (relationshipType == null) {
      if (other.relationshipType != null) {
        return false;
      }
    }
    else if (!relationshipType.equals(other.relationshipType)) {
      return false;
    }
    if (thisId == null) {
      if (other.thisId != null) {
        return false;
      }
    }
    else if (!thisId.equals(other.thisId)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Relationship [thisId=" + thisId + ", relatedId=" + relatedId + ", relationshipType=" + relationshipType + ", rootMetaObjectType=" + rootMetaObjectType + "]";
  }

}
