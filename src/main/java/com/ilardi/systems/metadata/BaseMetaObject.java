/**
 * Created Mar 30, 2018
 */
package com.ilardi.systems.metadata;

import javax.xml.bind.annotation.XmlType;

/**
 * @author rilardi
 *
 */

@XmlType(name = "BaseMetaObject")
public abstract class BaseMetaObject implements MetaObject {

  protected RootMetaObjectTypes rootMetaObjectType;

  public BaseMetaObject() {
    super();
  }

  public RootMetaObjectTypes getRootMetaObjectType() {
    return rootMetaObjectType;
  }

  public void setRootMetaObjectType(RootMetaObjectTypes rootMetaObjectType) {
    this.rootMetaObjectType = rootMetaObjectType;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((rootMetaObjectType == null) ? 0 : rootMetaObjectType.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof BaseMetaObject)) {
      return false;
    }
    BaseMetaObject other = (BaseMetaObject) obj;
    if (rootMetaObjectType != other.rootMetaObjectType) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "BaseMetaObject [rootMetaObjectType=" + rootMetaObjectType + "]";
  }

}
