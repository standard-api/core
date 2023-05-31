package ai.stapi.graph;

import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import ai.stapi.identity.UniqueIdentifier;
import java.util.List;

public interface EdgeLoader {

  List<TraversableEdge> loadEdges(UniqueIdentifier nodeId, String nodeType, String edgeType);

  List<TraversableEdge> loadEdges(UniqueIdentifier id, String nodeType);

  int getIdlessHashCodeForEdgesOnNode(UniqueIdentifier nodeId, String nodeType);

}
