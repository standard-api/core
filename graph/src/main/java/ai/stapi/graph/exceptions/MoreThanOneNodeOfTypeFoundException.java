package ai.stapi.graph.exceptions;

public class MoreThanOneNodeOfTypeFoundException extends GraphException {

  public MoreThanOneNodeOfTypeFoundException(String nodeType) {
    super(String.format("More than one node of type: \"%s\" found, but expected exactly one.",
        nodeType));
  }
}
