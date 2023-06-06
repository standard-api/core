package ai.stapi.graphoperations.graphReader.readResults;

import ai.stapi.graph.traversableGraphElements.TraversableNode;

public class NodeReadResult extends AbstractGraphElementReadResult {

  public NodeReadResult(TraversableNode node) {
    super(node);
  }

  public TraversableNode getNode() {
    return (TraversableNode) this.getGraphElement();
  }
}
