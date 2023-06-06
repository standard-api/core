package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;

public class RemovalNodeDescriptionParameters implements GraphDescriptionParameters {

  private final String nodeType;

  public RemovalNodeDescriptionParameters(String nodeType) {
    this.nodeType = nodeType;
  }

  public String getNodeType() {
    return nodeType;
  }
}