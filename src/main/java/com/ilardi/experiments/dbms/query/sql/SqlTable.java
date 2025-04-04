/**
 * Created Aug 10, 2016
 */
package com.ilardi.experiments.dbms.query.sql;

/**
 * @author rilardi
 *
 */

public class SqlTable extends BaseSqlElement {

  protected String database;

  protected String schema;

  protected String owner;

  protected String alias;

  public SqlTable() {
    super();
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getDatabase() {
    return database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

}
