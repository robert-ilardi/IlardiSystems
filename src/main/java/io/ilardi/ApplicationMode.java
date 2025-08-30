/**
 * Created Jun 15, 2024
 */
package io.ilardi;

import java.io.Serializable;

/**
 * @author robert.ilardi
 *
 */

public enum ApplicationMode implements Serializable {
  DEV, TEST, QA, SIT, UAT, PROD, COB, DR, DEFAULT
}
