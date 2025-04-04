/**
 * Created Mar 25, 2021
 */
package com.ilardi.systems.jefs;

import java.io.Serializable;

/**
 * @author robert.ilardi
 *
 */

public class PageChainAnchors implements Serializable {

  private JefsPage headPage;

  private JefsPage tailPage;

  public PageChainAnchors() {
    this(null, null);
  }

  public PageChainAnchors(JefsPage headPage, JefsPage tailPage) {
    this.headPage = headPage;
    this.tailPage = tailPage;
  }

  public JefsPage getHeadPage() {
    return headPage;
  }

  public void setHeadPage(JefsPage headPage) {
    this.headPage = headPage;
  }

  public JefsPage getTailPage() {
    return tailPage;
  }

  public void setTailPage(JefsPage tailPage) {
    this.tailPage = tailPage;
  }

  @Override
  public String toString() {
    return "PageChainAnchors [headPage=" + headPage + ", tailPage=" + tailPage + "]";
  }

}
