package ai.stapi.graph.inMemoryGraph.exceptions;

import ai.stapi.graph.exceptions.GraphException;

public class CannotCreateGraphWithOtherThanInputElements extends GraphException {

  public CannotCreateGraphWithOtherThanInputElements() {
    super(
        String.format(
            "Cannot create Graph with other than InputNode or InputEdge."
        )
    );
  }
}
