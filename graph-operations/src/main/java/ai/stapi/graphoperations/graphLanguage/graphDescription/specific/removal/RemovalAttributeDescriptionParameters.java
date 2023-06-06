package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;

public class RemovalAttributeDescriptionParameters implements GraphDescriptionParameters {

  private final String attributeName;

  public RemovalAttributeDescriptionParameters(String attributeName) {
    this.attributeName = attributeName;
  }

  public String getAttributeName() {
    return attributeName;
  }
}