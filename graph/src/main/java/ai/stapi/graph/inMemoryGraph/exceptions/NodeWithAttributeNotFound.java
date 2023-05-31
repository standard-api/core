package ai.stapi.graph.inMemoryGraph.exceptions;

import ai.stapi.graph.exceptions.GraphException;

public class NodeWithAttributeNotFound extends GraphException {

  public NodeWithAttributeNotFound() {
    super("Node with attribute not found.");
  }
}
