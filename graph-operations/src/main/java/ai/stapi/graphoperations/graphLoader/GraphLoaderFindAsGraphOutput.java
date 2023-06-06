package ai.stapi.graphoperations.graphLoader;

import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.identity.UniqueIdentifier;
import java.util.ArrayList;
import java.util.List;

public class GraphLoaderFindAsGraphOutput {

  private final List<UniqueIdentifier> foundGraphElementUuids;
  private final InMemoryGraphRepository graph;

  public GraphLoaderFindAsGraphOutput() {
    this(new ArrayList<>(), new InMemoryGraphRepository());
  }

  public GraphLoaderFindAsGraphOutput(List<UniqueIdentifier> foundGraphElementUuids,
      InMemoryGraphRepository graph) {
    this.foundGraphElementUuids = foundGraphElementUuids;
    this.graph = graph;
  }

  public List<UniqueIdentifier> getFoundGraphElementIds() {
    return foundGraphElementUuids;
  }

  public InMemoryGraphRepository getGraph() {
    return graph;
  }
}
