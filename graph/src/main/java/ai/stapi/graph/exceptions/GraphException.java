package ai.stapi.graph.exceptions;

import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;

public class GraphException extends RuntimeException {

  protected transient InMemoryGraphRepository graph;

  public GraphException(String message) {
    super(message);
    this.graph = new InMemoryGraphRepository();
  }

  public GraphException(String message, Throwable t) {
    super(message, t);
    this.graph = new InMemoryGraphRepository();
  }
}
