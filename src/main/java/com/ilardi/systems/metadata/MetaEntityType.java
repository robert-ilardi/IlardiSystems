/**
 * Created Mar 12, 2018
 */
package com.ilardi.systems.metadata;

import javax.xml.bind.annotation.XmlType;

/**
 * @author rilardi
 *
 */

@XmlType(name = "MetaEntityType")
public class MetaEntityType extends UserEnumerableType {

  protected MetaTypeIndicator typeIndicator;

  public MetaEntityType() {
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
    if (!(obj instanceof MetaEntityType)) {
      return false;
    }
    MetaEntityType other = (MetaEntityType) obj;
    if (typeIndicator != other.typeIndicator) {
      return false;
    }
    return true;
  }

}
