package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;

public class RemovalEdgeDescriptionParameters implements GraphDescriptionParameters {

  private final String edgeType;

  public RemovalEdgeDescriptionParameters(String edgeType) {
    this.edgeType = edgeType;
  }

  public String getEdgeType() {
    return edgeType;
  }
}