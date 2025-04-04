/**
 * Created Jun 20, 2017
 */
package com.ilardi.experiments.etl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

/**
 * @author rilardi
 *
 */

@XmlType(name = "RawStructure")
public class RawStructure implements Serializable {

  protected boolean arrayType;

  protected String name;

  protected String dataType;

  protected List<RawDataElement> dataElements;

  protected List<RawStructure> subStructures;

  public RawStructure() {}

  public boolean isArrayType() {
    return arrayType;
  }

  public void setArrayType(boolean arrayType) {
    this.arrayType = arrayType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public List<RawDataElement> getDataElements() {
    return dataElements;
  }

  public void setDataElements(List<RawDataElement> dataElements) {
    this.dataElements = dataElements;
  }

  public List<RawStructure> getSubStructures() {
    return subStructures;
  }

  public void setSubStructures(List<RawStructure> subStructures) {
    this.subStructures = subStructures;
  }

  public synchronized void addSubStructure(RawStructure subStruct) {
    if (subStructures == null) {
      subStructures = new ArrayList<RawStructure>();
    }

    subStructures.add(subStruct);
  }

  public synchronized int subStructureCount() {
    return (subStructures != null ? subStructures.size() : -1);
  }

  public synchronized void clearSubStructures() {
    if (subStructures != null) {
      subStructures.clear();
    }
  }

  public synchronized RawStructure getSubStructure(int index) {
    return subStructures.get(index);
  }

  public synchronized RawStructure removeSubStructure(int index) {
    return subStructures.remove(index);
  }

  public synchronized void addDataElement(RawDataElement dataElement) {
    if (dataElements == null) {
      dataElements = new ArrayList<RawDataElement>();
    }

    dataElements.add(dataElement);
  }

  public synchronized int dataElementCount() {
    return (dataElements != null ? dataElements.size() : -1);
  }

  public synchronized void clearDataElements() {
    if (dataElements != null) {
      dataElements.clear();
    }
  }

  public synchronized RawDataElement getDataElement(int index) {
    return dataElements.get(index);
  }

  public synchronized RawDataElement removeDataElement(int index) {
    return dataElements.remove(index);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (arrayType ? 1231 : 1237);
    result = prime * result + ((dataElements == null) ? 0 : dataElements.hashCode());
    result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((subStructures == null) ? 0 : subStructures.hashCode());
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
    if (getClass() != obj.getClass()) {
      return false;
    }
    RawStructure other = (RawStructure) obj;
    if (arrayType != other.arrayType) {
      return false;
    }
    if (dataElements == null) {
      if (other.dataElements != null) {
        return false;
      }
    }
    else if (!dataElements.equals(other.dataElements)) {
      return false;
    }
    if (dataType == null) {
      if (other.dataType != null) {
        return false;
      }
    }
    else if (!dataType.equals(other.dataType)) {
      return false;
    }
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    }
    else if (!name.equals(other.name)) {
      return false;
    }
    if (subStructures == null) {
      if (other.subStructures != null) {
        return false;
      }
    }
    else if (!subStructures.equals(other.subStructures)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "RawStructure [arrayType=" + arrayType + ", name=" + name + ", dataType=" + dataType + ", dataElements=" + dataElements + ", subStructures=" + subStructures + "]";
  }

}
