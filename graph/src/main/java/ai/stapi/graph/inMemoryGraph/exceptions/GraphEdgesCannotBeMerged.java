package ai.stapi.graph.inMemoryGraph.exceptions;

import ai.stapi.graph.exceptions.GraphException;
import ai.stapi.identity.UniqueIdentifier;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class GraphEdgesCannotBeMerged extends GraphException {

  protected GraphEdgesCannotBeMerged(String message) {
    super("Graph edges could not be merged, " + message);
  }

  public static GraphEdgesCannotBeMerged becauseTheyHaveDifferentTypes() {
    return new GraphEdgesCannotBeMerged("because they have different types.");
  }

  public static GraphEdgesCannotBeMerged becauseTheyHaveDifferentIds() {
    return new GraphEdgesCannotBeMerged("because they have different ids.");
  }

  public static GraphEdgesCannotBeMerged becauseTheyHaveDifferentNodeIds() {
    return new GraphEdgesCannotBeMerged("because one or both nodes have different ids.");
  }

  public static GraphEdgesCannotBeMerged becauseThereIsMultipleEdgesGivenEdgeCouldBeMergeWith(
      List<UniqueIdentifier> possibleEdgesToMergeWith) {
    return new GraphEdgesCannotBeMerged(
        "There is multiple edges with which given edge could be merged with."
            + System.lineSeparator()
            + "Possible edges to be merged with: " +
            "[" + StringUtils.join(possibleEdgesToMergeWith, "], [") + "]."
    );
  }
}
