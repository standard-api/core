package ai.stapi.graphoperations.exampleImplementations;

import ai.stapi.graph.Graph;

public class ExampleGraphUpdatedEvent {
  
  private final Graph graph;

  public ExampleGraphUpdatedEvent(Graph graph) {
    this.graph = graph;
  }

  public Graph getGraph() {
    return graph;
  }
}
