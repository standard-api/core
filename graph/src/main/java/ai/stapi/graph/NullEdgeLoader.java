package ai.stapi.graph;

import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import ai.stapi.identity.UniqueIdentifier;
import java.util.ArrayList;
import java.util.List;

public class NullEdgeLoader implements EdgeLoader {


  @Override
  public List<TraversableEdge> loadEdges(UniqueIdentifier nodeId, String nodeType,
      String edgeType) {
    return new ArrayList<>();
  }

  @Override
  public List<TraversableEdge> loadEdges(UniqueIdentifier id, String nodeType) {
    return new ArrayList<>();
  }

  @Override
  public int getIdlessHashCodeForEdgesOnNode(UniqueIdentifier nodeId, String nodeType) {
    return 0;
  }
}
