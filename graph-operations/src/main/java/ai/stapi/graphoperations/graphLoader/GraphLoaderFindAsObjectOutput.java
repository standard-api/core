package ai.stapi.graphoperations.graphLoader;

import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.identity.UniqueIdentifier;
import java.util.ArrayList;
import java.util.List;

public class GraphLoaderFindAsObjectOutput<ReturnObjectType> {

  private final List<ReturnObjectType> data;
  private final GraphLoaderFindAsGraphOutput graphLoaderFindAsGraphOutput;

  public GraphLoaderFindAsObjectOutput() {
    this(new ArrayList<>(), new ArrayList<>(), new InMemoryGraphRepository());
  }

  public GraphLoaderFindAsObjectOutput(
      List<ReturnObjectType> data,
      List<UniqueIdentifier> foundGraphElementUUIDs,
      InMemoryGraphRepository graph
  ) {
    this.data = data;
    this.graphLoaderFindAsGraphOutput = new GraphLoaderFindAsGraphOutput(foundGraphElementUUIDs, graph);
  }

  public List<ReturnObjectType> getData() {
    return data;
  }

  public GraphLoaderFindAsGraphOutput getGraphLoaderFindAsGraphOutput() {
    return graphLoaderFindAsGraphOutput;
  }
}
