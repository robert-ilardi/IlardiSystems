/**
 * Created Aug 5, 2026
 */
package io.jdbcaf;

/**
 * @author Kate Ilardi
 */

public interface ConnectionClient {

  public void connect() throws JdbcAfException;

  public void close() throws JdbcAfException;

  public boolean isClosed() throws JdbcAfException;

  public JdbcAfJdbcResponse execute(JdbcAfJdbcRequest jdbcAfReq) throws JdbcAfException;

}
