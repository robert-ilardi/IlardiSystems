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

@XmlType(name = "RawRecord")
public class RawRecord implements Serializable {

  protected RawHeader headerData;

  protected List<RawStructure> structures;

  public RawRecord() {}

  public RawHeader getHeaderData() {
    return headerData;
  }

  public void setHeaderData(RawHeader headerData) {
    this.headerData = headerData;
  }

  public List<RawStructure> getStructures() {
    return structures;
  }

  public void setStructures(List<RawStructure> structures) {
    this.structures = structures;
  }

  public synchronized void addStructure(RawStructure rawStruct) {
    if (structures == null) {
      structures = new ArrayList<RawStructure>();
    }

    structures.add(rawStruct);
  }

  public synchronized int structureCount() {
    return (structures != null ? structures.size() : -1);
  }

  public synchronized void clearStructures() {
    if (structures != null) {
      structures.clear();
    }
  }

  public synchronized RawStructure getStructure(int index) {
    return structures.get(index);
  }

  public synchronized RawStructure removeStructure(int index) {
    return structures.remove(index);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((headerData == null) ? 0 : headerData.hashCode());
    result = prime * result + ((structures == null) ? 0 : structures.hashCode());
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
    RawRecord other = (RawRecord) obj;
    if (headerData == null) {
      if (other.headerData != null) {
        return false;
      }
    }
    else if (!headerData.equals(other.headerData)) {
      return false;
    }
    if (structures == null) {
      if (other.structures != null) {
        return false;
      }
    }
    else if (!structures.equals(other.structures)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "RawRecord [headerData=" + headerData + ", structures=" + structures + "]";
  }

}
