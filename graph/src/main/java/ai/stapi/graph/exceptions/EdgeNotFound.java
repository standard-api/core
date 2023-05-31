package ai.stapi.graph.exceptions;

import ai.stapi.identity.UniqueIdentifier;


public class EdgeNotFound extends GraphException {

  public EdgeNotFound(UniqueIdentifier edgeId, String edgeType) {
    super(
        String.format(
            "Edge with id \"%s\" of type \"%s\" not found.",
            edgeId.toString(),
            edgeType
        )
    );
  }

  public EdgeNotFound(UniqueIdentifier edgeId) {
    super(
        String.format(
            "Edge with id \"%s\" not found in any collection.",
            edgeId.toString()
        )
    );
  }
}
