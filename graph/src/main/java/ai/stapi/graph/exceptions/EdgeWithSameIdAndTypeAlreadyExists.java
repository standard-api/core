package ai.stapi.graph.exceptions;

public class EdgeWithSameIdAndTypeAlreadyExists extends GraphException {

  public EdgeWithSameIdAndTypeAlreadyExists(String edgeId, String edgeType) {
    super(
        "Edge can not be saved, because edge with same id [" + edgeId
            + "] and type [" + edgeType + "] already exists."
            + "You can use replace instead."
    );
  }
}
