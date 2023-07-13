package ai.stapi.graph;

import ai.stapi.graph.graphElementForRemoval.NodeForRemoval;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.identity.UniqueIdentifier;
import java.util.List;

public interface NodeRepository {

  void save(Node node);

  TraversableNode loadNode(UniqueIdentifier UniqueIdentifier, String nodeType);

  void replace(Node node);

  void removeNode(UniqueIdentifier id, String nodeType);

  void removeNode(NodeForRemoval nodeForRemoval);

  boolean nodeExists(UniqueIdentifier id, String nodeType);

  List<NodeTypeInfo> getNodeTypeInfos();

  List<NodeInfo> getNodeInfosBy(String nodeType);
  
}
