package ai.stapi.graphoperations.objectGraphMapper.model;

import ai.stapi.graph.graphElementForRemoval.GraphElementForRemoval;
import ai.stapi.graph.Graph;
import java.util.ArrayList;
import java.util.List;

public class GraphMappingResult {

  private final List<GraphElementForRemoval> elementForRemoval;
  private final Graph graph;

  public GraphMappingResult(
      Graph graph,
      List<GraphElementForRemoval> elementForRemoval
  ) {
    this.elementForRemoval = elementForRemoval;
    this.graph = graph;
  }

  public List<GraphElementForRemoval> getElementForRemoval() {
    return elementForRemoval;
  }

  public Graph getGraph() {
    return graph;
  }

  public GraphMappingResult merge(GraphMappingResult anotherMappingResult) {
    var mergedList = new ArrayList<>(this.elementForRemoval);
    mergedList.addAll(anotherMappingResult.elementForRemoval);
    return new GraphMappingResult(
        this.getGraph().merge(anotherMappingResult.graph),
        mergedList
    );
  }
}
