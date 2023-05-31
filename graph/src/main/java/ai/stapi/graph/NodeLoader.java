package ai.stapi.graph;

import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.identity.UniqueIdentifier;

public interface NodeLoader {

  TraversableNode loadNode(UniqueIdentifier nodeId, String nodeType);
}
