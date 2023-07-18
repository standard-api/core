package ai.stapi.graph.exceptions;

import ai.stapi.graph.graphelements.Edge;

public class OneOrBothNodesOnEdgeDoesNotExist extends GraphException {

  public OneOrBothNodesOnEdgeDoesNotExist(Edge edge) {
    super(
        String.format(
            "One or both nodes on edge you are trying to save does not exist. Create them first. Details: \n" +
                "Node From: id: %s ; type: %s \n" +
                "Node To: id: %s ; type: %s \n",
            edge.getNodeFromId().getId(),
            edge.getNodeFromType(),
            edge.getNodeToId().getId(),
            edge.getNodeToType()
        )
    );
  }
}
