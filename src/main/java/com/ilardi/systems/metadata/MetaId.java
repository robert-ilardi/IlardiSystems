/**
 * Created Jun 30, 2016
 */
package com.ilardi.systems.metadata;

import javax.xml.bind.annotation.XmlType;

/**
 * @author rilardi
 *
 */

@XmlType(name = "MetaId")
public class MetaId extends BaseMetaObject {

  protected Long id;
  protected MetaIdType idType;

  public MetaId() {}

  public MetaId(Long id, MetaIdType idType) {
    this.id = id;
    this.idType = idType;
  }

  public MetaId(MetaId id) {
    this.id = id.id;
    this.idType = id.idType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public MetaIdType getIdType() {
    return idType;
  }

  public void setIdType(MetaIdType idType) {
    this.idType = idType;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((idType == null) ? 0 : idType.hashCode());
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
    if (!(obj instanceof MetaId)) {
      return false;
    }
    MetaId other = (MetaId) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    }
    else if (!id.equals(other.id)) {
      return false;
    }
    if (idType != other.idType) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "MetaId [id=" + id + ", idType=" + idType + ", rootMetaObjectType=" + rootMetaObjectType + "]";
  }

}
