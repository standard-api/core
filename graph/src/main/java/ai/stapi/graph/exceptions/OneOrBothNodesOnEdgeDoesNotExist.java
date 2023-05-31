package ai.stapi.graph.exceptions;

public class OneOrBothNodesOnEdgeDoesNotExist extends GraphException {

  public OneOrBothNodesOnEdgeDoesNotExist() {
    super("One or both nodes on edge you are trying to save does not exist. Create them first.");
  }
}
