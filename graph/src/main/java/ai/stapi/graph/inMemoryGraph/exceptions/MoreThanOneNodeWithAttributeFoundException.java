package ai.stapi.graph.inMemoryGraph.exceptions;

import ai.stapi.graph.exceptions.GraphException;

public class MoreThanOneNodeWithAttributeFoundException extends GraphException {

  public MoreThanOneNodeWithAttributeFoundException() {
    super("More than one node with attribute found, but expected only one");
  }
}
