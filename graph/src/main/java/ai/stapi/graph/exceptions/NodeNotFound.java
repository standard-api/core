package ai.stapi.graph.exceptions;

import ai.stapi.identity.UniqueIdentifier;


public class NodeNotFound extends GraphException {

  public NodeNotFound(UniqueIdentifier nodeId, String nodeType) {
    super(
        String.format(
            "Node with id \"%s\" of type \"%s\" not found.",
            nodeId.toString(),
            nodeType
        )
    );
  }

  public NodeNotFound(UniqueIdentifier nodeId) {
    super(
        String.format(
            "Node with id \"%s\" not found in any collection.",
            nodeId.toString()
        )
    );
  }
}
