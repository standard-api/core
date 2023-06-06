package ai.stapi.graphoperations.graphLoader;

import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;

public class GraphLoaderGetAsObjectOutput<ReturnObjectType> {

  private final ReturnObjectType data;
  private final InMemoryGraphRepository graph;

  public GraphLoaderGetAsObjectOutput(
      ReturnObjectType data,
      InMemoryGraphRepository graph
  ) {
    this.data = data;
    this.graph = graph;
  }

  public ReturnObjectType getData() {
    return data;
  }

  public InMemoryGraphRepository getGraph() {
    return graph;
  }
}
