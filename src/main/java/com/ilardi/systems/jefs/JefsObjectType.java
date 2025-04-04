/**
 * Created Feb 25, 2021
 */
package com.ilardi.systems.jefs;

import java.io.Serializable;

/**
 * @author robert.ilardi
 *
 */

public enum JefsObjectType implements Serializable {
  DIRECTORY, FILE, FREESPACE, DATAPAGE, PAGE_ALLOCATION_TABLE, FILE_ALLOCATION_TABLE, MEMORY_MANAGEMENT_SEGMENT, VARIABLE;
}
