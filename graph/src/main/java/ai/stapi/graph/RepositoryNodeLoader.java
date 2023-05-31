package ai.stapi.graph;

import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.identity.UniqueIdentifier;

public class RepositoryNodeLoader implements NodeLoader {

  private final NodeRepository nodeRepository;

  public RepositoryNodeLoader(NodeRepository nodeRepository) {
    this.nodeRepository = nodeRepository;
  }

  @Override
  public TraversableNode loadNode(UniqueIdentifier nodeId, String nodeType) {
    return this.nodeRepository.loadNode(
        nodeId,
        nodeType
    );
  }
}
