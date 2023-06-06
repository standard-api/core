package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;

public class AttributeDescriptionParameters implements GraphDescriptionParameters {
  
  private String attributeName;

  private AttributeDescriptionParameters() {
  }

  public AttributeDescriptionParameters(String attributeName) {
    this.attributeName = attributeName;
  }

  public String getAttributeName() {
    return attributeName;
  }
}