/**
 * Created Jul 28, 2026
 */
package io.jdbcaf.drivers;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author Kate Ilardi
 */

public class JdbcAfDriver implements JdbcAfJdbcObject, Driver {

  public static final String JDBC_DRIVER_URL_PREFIX = "";

  public static final int MAJOR_VERSION = 0;

  public static final int MINOR_VERSION = 1;

  public static final boolean FULLY_JDBC_COMPLIANT = false;

  private final Object driverLock;

  public JdbcAfDriver() {
    super();

    driverLock = new Object();
  }

  @Override
  public Connection connect(String url, Properties info) throws SQLException {
    JdbcAfConnection jAfConn = null;

    return jAfConn;
  }

  @Override
  public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
    DriverPropertyInfo[] dpiArr;

    dpiArr = new DriverPropertyInfo[0];

    return dpiArr;
  }

  @Override
  public boolean acceptsURL(String url) throws SQLException {
    return (url != null && url.trim().toLowerCase().startsWith(JDBC_DRIVER_URL_PREFIX));
  }

  @Override
  public int getMajorVersion() {
    return MAJOR_VERSION;
  }

  @Override
  public int getMinorVersion() {
    return MINOR_VERSION;
  }

  @Override
  public boolean jdbcCompliant() {
    return FULLY_JDBC_COMPLIANT;
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    throw new SQLFeatureNotSupportedException();
  }

}
