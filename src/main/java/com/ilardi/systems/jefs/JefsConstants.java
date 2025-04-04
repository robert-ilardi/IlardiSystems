/**
 * Created Feb 28, 2021
 */
package com.ilardi.systems.jefs;

/**
 * @author robert.ilardi
 *
 */

public final class JefsConstants {

  public static final int JEFS_VERSION = 1;

  public static final int VOLUME_HEADER_SIZE = 1024;

  public static final int DEFAULT_PAGE_HEADER_SIZE = 1024;

  public static final int DEFAULT_PAGE_DATA_SIZE = 4096;

  public static final String JEFS_MAGIC_BYTES = "JEFSVOL$";

  public static final int FIRST_OBJECT_ID = 1000;

  public static final String DEFAULT_VOLUME_NAME = "NewJefsVolume";

  public static final int FIRST_OBJECT_POS = 1024;

  // VOLUMEN HEADER Constants----------------------------->

  public static final int VOL_MAGIC_BYTES_POS = 0;
  public static final int VOL_MAGIC_BYTES_LEN = 8;

  public static final int VOL_VERSION_POS = 8;
  public static final int VOL_VERSION_LEN = 4;

  public static final int VOL_VOLUME_NAME_POS = 12;
  public static final int VOL_VOLUME_NAME_LEN = 32;

  public static final int VOL_PAGE_HEADER_SIZE_POS = 44;
  public static final int VOL_PAGE_HERADER_SIZE_LEN = 4;

  public static final int VOL_PAGE_DATA_SIZE_POS = 48;
  public static final int VOL_PAGE_DATA_SIZE_LEN = 4;

  public static final int VOL_CREATION_TS_POS = 52;
  public static final int VOL_CREATION_TS_LEN = 8;

  public static final int VOL_MODIFICATION_TS_POS = 60;
  public static final int VOL_MODIFICATION_TS_LEN = 8;

  public static final int VOL_NEXT_OBJECT_ID_POS = 68;
  public static final int VOL_NEXT_OBJECT_ID_LEN = 8;

  public static final int VOL_FREE_CHAIN_BEGIN_POS = 76;
  public static final int VOL_FREE_CHAIN_BEGIN_LEN = 8;

  public static final int VOL_FREE_CHAIN_END_POS = 84;
  public static final int VOL_FREE_CHAIN_END_LEN = 8;

  // PAGE HEADER Constants------------------------------->

  public static final int PAGE_OBJECT_ID_OFFSET = 0;
  public static final int PAGE_OBJECT_ID_LEN = 8;

  public static final int PAGE_PARENT_OBJECT_ID_OFFSET = 8;
  public static final int PAGE_PARENT_OBJECT_ID_LEN = 8;

  public static final int PAGE_OBJECT_NANE_OFFSET = 16;
  public static final int PAGE_OBJECT_NAME_LEN = 128;

  public static final int PAGE_OBJECT_TYPE_OFFSET = 144;
  public static final int PAGE_OBJECT_TYPE_LEN = 32;

  public static final int PAGE_OBJECT_STATUS_OFFSET = 176;
  public static final int PAGE_OBJECT_STATUS_LEN = 32;

  public static final int PAGE_USED_DATA_SIZE_OFFSET = 208;
  public static final int PAGE_USED_DATA_SIZE_LEN = 4;

  public static final int PAGE_CREATED_TS_OFFSET = 212;
  public static final int PAGE_CREATED_TS_LEN = 8;

  public static final int PAGE_MODIFIED_TS_OFFSET = 220;
  public static final int PAGE_MODIFIED_TS_LEN = 8;

  public static final int PAGE_OBJECT_PAGE_INDEX_OFFSET = 228;
  public static final int PAGE_OBJECT_PAGE_INDEX_LEN = 8;

  public static final int PAGE_NEXT_PAGE_POS_OFFSET = 236;
  public static final int PAGE_NEXT_PAGE_POS_LEN = 8;

  public static final int PAGE_PARENT_PAGE_POS_OFFSET = 244;
  public static final int PAGE_PARENT_PAGE_POS_LEN = 8;

  // PAT Constants------------------------------->

  public static final String JEFS_ROOT_PAT_NAME = "[ROOT-PAGE-ALLOCATION-TABLE]";

  public static final int PAT_PAGE_ENTRY_LEN = PAGE_NEXT_PAGE_POS_LEN + PAGE_OBJECT_ID_LEN;

  // FAT Constants------------------------------->

  public static final String JEFS_FAT_PAGE_NAME = "[FILE-ALLOCATION-TABLE]";

  public static final String UNIX_PATH_SEPARATOR = "/";

  public static final String UNIX_PATH_SEPARATOR_REGEX = "\\/";

  public static final String DOS_PATH_SEPARATOR = "\\";

  public static final String DOS_PATH_SEPARATOR_REGEX = "\\\\";

  public static final String JEFS_ROOT_DIRECTORY = UNIX_PATH_SEPARATOR;

  public static final String JEFS_PATH_SEPARATOR = UNIX_PATH_SEPARATOR;

  public static final String JEFS_PATH_SEPARATOR_REGEX = UNIX_PATH_SEPARATOR_REGEX;

  public static final int FAT_PAGE_ENTRY_LEN = PAGE_NEXT_PAGE_POS_LEN + PAGE_OBJECT_ID_LEN + PAGE_PARENT_OBJECT_ID_LEN + PAGE_PARENT_PAGE_POS_LEN;

  // File Heap Constants---------------------------->

  public static final String JEFS_MMS_PAGE_NAME = "[MEMORY-MANAGEMENT-SEGMENT]";

  public static final int MMS_PAGE_ENTRY_LEN = PAGE_NEXT_PAGE_POS_LEN + PAGE_OBJECT_ID_LEN;

}
