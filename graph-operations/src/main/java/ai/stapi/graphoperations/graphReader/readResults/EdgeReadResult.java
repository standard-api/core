package ai.stapi.graphoperations.graphReader.readResults;

import ai.stapi.graph.traversableGraphElements.TraversableEdge;

public class EdgeReadResult extends AbstractGraphElementReadResult {

  public EdgeReadResult(TraversableEdge edge) {
    super(edge);
  }

  public TraversableEdge getEdge() {
    return (TraversableEdge) this.getGraphElement();
  }

}
