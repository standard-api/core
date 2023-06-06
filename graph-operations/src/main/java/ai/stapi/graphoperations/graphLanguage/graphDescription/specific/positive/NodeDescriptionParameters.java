package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;

public class NodeDescriptionParameters implements GraphDescriptionParameters {

  private String nodeType;

  private NodeDescriptionParameters() {

  }

  public NodeDescriptionParameters(String nodeType) {
    this.nodeType = nodeType;
  }

  public String getNodeType() {
    return nodeType;
  }
}