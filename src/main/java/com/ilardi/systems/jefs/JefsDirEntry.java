/**
 * Created Mar 13, 2021
 */
package com.ilardi.systems.jefs;

import java.util.ArrayList;

/**
 * @author robert.ilardi
 *
 */

public class JefsDirEntry {

  private JefsPageHeader header;

  private JefsDirEntry parent;

  private ArrayList<JefsDirEntry> entries;

  public JefsDirEntry() {}

  public JefsPageHeader getHeader() {
    return header;
  }

  public void setHeader(JefsPageHeader header) {
    this.header = header;
  }

  public ArrayList<JefsDirEntry> getEntries() {
    return entries;
  }

  public void setEntries(ArrayList<JefsDirEntry> entries) {
    this.entries = entries;
  }

  public JefsDirEntry getParent() {
    return parent;
  }

  public void setParent(JefsDirEntry parent) {
    this.parent = parent;
  }

  @Override
  public String toString() {
    return "JefsDirEntry [header=" + header + ", entries=" + entries + "]";
  }

}
