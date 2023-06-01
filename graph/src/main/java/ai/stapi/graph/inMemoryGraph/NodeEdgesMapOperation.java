package ai.stapi.graph.inMemoryGraph;

import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import ai.stapi.identity.UniqueIdentifier;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class NodeEdgesMapOperation {

  public static ConcurrentHashMap<UniqueIdentifier, ConcurrentSkipListSet<TraversableEdge>> create(
      InMemoryGraphRepository graph
  ) {
    ConcurrentHashMap<UniqueIdentifier, ConcurrentSkipListSet<TraversableEdge>> map =
        new ConcurrentHashMap<>();
    for (var edge : graph.loadAllEdges()) {
      map = NodeEdgesMapOperation.addEdgeToNodeInRelationMap(map, edge, edge.getNodeFromId());
      map = NodeEdgesMapOperation.addEdgeToNodeInRelationMap(map, edge, edge.getNodeToId());
    }
    return map;
  }

  public static ConcurrentSkipListSet<TraversableEdge> getNodeEdges(
      ConcurrentHashMap<UniqueIdentifier, ConcurrentSkipListSet<TraversableEdge>> map,
      UniqueIdentifier nodeId,
      String nodeType
  ) {
    if (map.containsKey(nodeId)) {
      return map.get(nodeId);
    }
    return new ConcurrentSkipListSet();
  }

  public static ConcurrentHashMap<UniqueIdentifier, ConcurrentSkipListSet<TraversableEdge>> upsertEdge(
      ConcurrentHashMap<UniqueIdentifier, ConcurrentSkipListSet<TraversableEdge>> map,
      TraversableEdge edge
  ) {
    map = NodeEdgesMapOperation.addEdgeToNodeInRelationMap(map, edge, edge.getNodeFromId());
    map = NodeEdgesMapOperation.addEdgeToNodeInRelationMap(map, edge, edge.getNodeToId());
    return map;
  }

  public static ConcurrentHashMap<UniqueIdentifier, ConcurrentSkipListSet<TraversableEdge>> removeEdge(
      ConcurrentHashMap<UniqueIdentifier, ConcurrentSkipListSet<TraversableEdge>> map,
      Edge edge
  ) {
    map = NodeEdgesMapOperation.removeEdgeFromNodeInRelationMap(map, edge.getId(),
        edge.getNodeFromId());
    map = NodeEdgesMapOperation.removeEdgeFromNodeInRelationMap(map, edge.getId(),
        edge.getNodeToId());
    return map;
  }

  private static ConcurrentHashMap<UniqueIdentifier, ConcurrentSkipListSet<TraversableEdge>> addEdgeToNodeInRelationMap(
      ConcurrentHashMap<UniqueIdentifier, ConcurrentSkipListSet<TraversableEdge>> originalMap,
      TraversableEdge traversableEdge,
      UniqueIdentifier nodeId
  ) {
    ConcurrentSkipListSet<TraversableEdge> set = new ConcurrentSkipListSet<>();
    if (originalMap.containsKey(nodeId)) {
      set = originalMap.get(nodeId);
    } else {
      originalMap.put(nodeId, set);
    }
    set.add(traversableEdge);

    return originalMap;
  }

  private static ConcurrentHashMap<UniqueIdentifier, ConcurrentSkipListSet<TraversableEdge>> removeEdgeFromNodeInRelationMap(
      ConcurrentHashMap<UniqueIdentifier, ConcurrentSkipListSet<TraversableEdge>> originalMap,
      UniqueIdentifier edgeId,
      UniqueIdentifier nodeId
  ) {
    if (!originalMap.containsKey(nodeId)) {
      throw new RuntimeException(
          "Trying to remove edge %s from nodeEdge map, but node %s is not present");
    }
    originalMap.get(nodeId).removeIf(
        traversableEdge -> edgeId.equals(traversableEdge.getId())
    );

    return originalMap;
  }

}
