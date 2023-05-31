package ai.stapi.graph.graphElementForRemoval;

import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import ai.stapi.identity.UniqueIdentifier;

public class EdgeForRemoval implements GraphElementForRemoval {

  public static final String ELEMENT_BASE_TYPE = "EDGE";
  private final UniqueIdentifier edgeId;
  private final String edgeType;

  public EdgeForRemoval(UniqueIdentifier edgeId, String edgeType) {
    this.edgeId = edgeId;
    this.edgeType = edgeType;
  }

  public EdgeForRemoval(TraversableEdge edge) {
    this.edgeId = edge.getId();
    this.edgeType = edge.getType();
  }

  @Override
  public UniqueIdentifier getGraphElementId() {
    return this.edgeId;
  }

  @Override
  public String getGraphElementType() {
    return this.edgeType;
  }

  @Override
  public String getGraphElementBaseType() {
    return ELEMENT_BASE_TYPE;
  }
}
