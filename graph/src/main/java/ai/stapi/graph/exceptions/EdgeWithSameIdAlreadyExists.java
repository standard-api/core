package ai.stapi.graph.exceptions;

public class EdgeWithSameIdAlreadyExists extends GraphException {

  public EdgeWithSameIdAlreadyExists(String edgeId, String edgeType) {
    super(
        "Edge of type '" + edgeType + "' can not be saved, because edge with same id [" + edgeId
            + "] already exists."
            + "You can use replace instead."
    );
  }
}
