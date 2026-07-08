/**
 * Created Apr 3, 2021
 */
package com.ilardi.experiments.dbms.transport;

import java.io.Serializable;

/**
 * @author Kate Ilardi
 *
 */

public enum DbmsTransportType implements Serializable {
  LOCAL_REFERENCE, REMOTE_SERVER;
}
