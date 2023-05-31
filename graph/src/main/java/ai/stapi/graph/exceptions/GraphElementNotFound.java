package ai.stapi.graph.exceptions;

import ai.stapi.identity.UniqueIdentifier;


public class GraphElementNotFound extends GraphException {

  public GraphElementNotFound(UniqueIdentifier graphElementId) {
    super(
        String.format(
            "Graph element with id \"%s\" not found.",
            graphElementId
        )
    );
  }

}
