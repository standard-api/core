package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;

public class EdgeDescriptionParameters implements GraphDescriptionParameters {

  private String edgeType;

  private EdgeDescriptionParameters() {
  }

  public EdgeDescriptionParameters(String edgeType) {
    this.edgeType = edgeType;
  }

  public String getEdgeType() {
    return edgeType;
  }
}
