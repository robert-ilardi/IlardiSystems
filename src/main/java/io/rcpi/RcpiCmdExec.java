/**
 * Created Aug 26, 2024
 */
package io.rcpi;

import io.rcpi.model.RcpiCmdArg;
import io.rcpi.model.RcpiCmdExitStatus;

/**
 * @author robert.ilardi
 */

public interface RcpiCmdExec {

  public void init() throws RcpiException;

  public RcpiCmdExitStatus execute(RcpiStdIn rcpiStdIn, RcpiStdOut rcpiStdOut, RcpiStdErr rcpiStdErr, RcpiCmdArg[] args) throws RcpiException;

  public void shutdown() throws RcpiException;

}
