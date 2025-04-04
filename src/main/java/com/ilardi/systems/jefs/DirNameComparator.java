/**
 * Created Mar 14, 2021
 */
package com.ilardi.systems.jefs;

import java.util.Comparator;

/**
 * @author robert.ilardi
 *
 */

public class DirNameComparator implements Comparator<JefsDirEntry> {

  public DirNameComparator() {}

  @Override
  public int compare(JefsDirEntry o1, JefsDirEntry o2) {
    String on1, on2;

    on1 = o1.getHeader().getObjectName();
    on2 = o2.getHeader().getObjectName();

    return on1.compareTo(on2);
  }
}
