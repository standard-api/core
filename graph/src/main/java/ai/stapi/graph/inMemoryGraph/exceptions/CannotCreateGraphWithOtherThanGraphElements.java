package ai.stapi.graph.inMemoryGraph.exceptions;

import ai.stapi.graph.exceptions.GraphException;

public class CannotCreateGraphWithOtherThanGraphElements extends GraphException {

  public CannotCreateGraphWithOtherThanGraphElements() {
    super(
        String.format(
            "Cannot create Graph with other than Node or Edge."
        )
    );
  }
}
