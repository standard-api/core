package ai.stapi.graph.inMemoryGraph.exceptions;

import ai.stapi.graph.exceptions.GraphException;

public class GraphNodesCannotBeMerged extends GraphException {

  protected GraphNodesCannotBeMerged(String message) {
    super("Graph nodes could not be merged, " + message);
  }

  public static GraphNodesCannotBeMerged becauseTheyHaveDifferentTypes(
      String originalType,
      String newType
  ) {
    return new GraphNodesCannotBeMerged(
        String.format(
            "because they have different types. Original: '%s'. New: '%s'",
            originalType,
            newType
        )
    );
  }

  public static GraphNodesCannotBeMerged becauseTheyHaveDifferentIds() {
    return new GraphNodesCannotBeMerged("because they have different ids");
  }
}
