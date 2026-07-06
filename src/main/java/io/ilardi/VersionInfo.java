/**
 * Created Oct 24, 2025
 */
package io.ilardi;

/**
 * @author kilardi
 */

public class VersionInfo implements ValueObject {

  private String version;
  private String name;
  private String description;
  private String copyright;

  public VersionInfo() {
    super();
  }

  @Override
  public int compareTo(ValueObject other) {
    // TODO Auto-generated method stub
    return 0;
  }

}
