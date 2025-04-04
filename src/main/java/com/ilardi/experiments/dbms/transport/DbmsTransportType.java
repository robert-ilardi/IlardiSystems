/**
 * Created Apr 3, 2021
 */
package com.ilardi.experiments.dbms.transport;

import java.io.Serializable;

/**
 * @author robert.ilardi
 *
 */

public enum DbmsTransportType implements Serializable {
  LOCAL_REFERENCE, REMOTE_SERVER;
}
