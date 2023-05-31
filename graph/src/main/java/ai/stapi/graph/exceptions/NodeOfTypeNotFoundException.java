package ai.stapi.graph.exceptions;

public class NodeOfTypeNotFoundException extends GraphException {

  public NodeOfTypeNotFoundException(String nodeType) {
    super(String.format("Node of type: \"%s\" not found.", nodeType));
  }
}
