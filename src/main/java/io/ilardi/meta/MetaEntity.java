/**
 * Created Oct 24, 2025
 */
package io.ilardi.meta;

/**
 * @author Kate Ilardi
 */

public class MetaEntity extends BaseMetaModelObject {

  protected String globallyUniqueId;

  protected String entityMasterId;
  protected String entityRevisionId;

  protected String primaryName;
  protected String primaryDescription;

  protected Boolean singletonEntity;

  public MetaEntity() {
    super();
  }

}
