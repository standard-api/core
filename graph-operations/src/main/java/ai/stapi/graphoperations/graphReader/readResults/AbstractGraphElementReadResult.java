package ai.stapi.graphoperations.graphReader.readResults;

import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;

public class AbstractGraphElementReadResult implements ReadResult {

  private final TraversableGraphElement graphElement;

  public AbstractGraphElementReadResult(TraversableGraphElement graphElement) {
    this.graphElement = graphElement;
  }

  public TraversableGraphElement getGraphElement() {
    return graphElement;
  }
}
