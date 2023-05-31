package ai.stapi.graph;

import ai.stapi.graph.graphElementForRemoval.NodeForRemoval;
import ai.stapi.graph.inputGraphElements.InputNode;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.identity.UniqueIdentifier;

import java.util.List;

public interface NodeRepository {

  void save(InputNode node);

  TraversableNode loadNode(UniqueIdentifier UniqueIdentifier, String nodeType);

  TraversableNode loadNode(UniqueIdentifier UniqueIdentifier);

  void replace(InputNode node);

  void removeNode(UniqueIdentifier id, String nodeType);

  void removeNode(NodeForRemoval nodeForRemoval);

  boolean nodeExists(UniqueIdentifier id, String nodeType);

  List<NodeTypeInfo> getNodeTypeInfos();

  List<NodeInfo> getNodeInfosBy(String nodeType);
  
}
