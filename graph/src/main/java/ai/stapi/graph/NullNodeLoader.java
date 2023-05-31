package ai.stapi.graph;

import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.identity.UniqueIdentifier;

public class NullNodeLoader implements NodeLoader {

  @Override
  public TraversableNode loadNode(UniqueIdentifier nodeId, String nodeType) {
    return null;
  }
}
