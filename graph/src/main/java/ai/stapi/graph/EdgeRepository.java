package ai.stapi.graph;

import ai.stapi.graph.graphElementForRemoval.EdgeForRemoval;
import ai.stapi.graph.inputGraphElements.InputEdge;
import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import ai.stapi.identity.UniqueIdentifier;
import java.util.List;
import java.util.Set;

public interface EdgeRepository {

  void save(InputEdge inputEdge);

  TraversableEdge loadEdge(UniqueIdentifier id, String type);

  boolean edgeExists(UniqueIdentifier id, String type);

  void replace(InputEdge inputEdge);

  void removeEdge(UniqueIdentifier edgeId, String edgeType);

  void removeEdge(EdgeForRemoval edgeForRemoval);

  List<EdgeTypeInfo> getEdgeTypeInfos();

  Set<TraversableEdge> findInAndOutEdgesForNode(UniqueIdentifier nodeId, String nodeType);

  TraversableEdge findEdgeByTypeAndNodes(String edgeType, NodeIdAndType nodeFrom,
                                         NodeIdAndType nodeTo);
}
