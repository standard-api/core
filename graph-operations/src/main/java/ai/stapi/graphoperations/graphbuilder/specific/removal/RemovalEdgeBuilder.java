package ai.stapi.graphoperations.graphbuilder.specific.removal;

import ai.stapi.graphoperations.graphbuilder.exception.GraphBuilderException;
import ai.stapi.graph.graphElementForRemoval.EdgeForRemoval;
import ai.stapi.identity.UniqueIdentifier;

public class RemovalEdgeBuilder implements GraphElementForRemovalBuilder {

  private UniqueIdentifier id;
  private String type;

  @Override
  public RemovalEdgeBuilder setId(UniqueIdentifier id) {
    this.id = id;
    return this;
  }

  public RemovalEdgeBuilder setId(String id) {
    this.id = new UniqueIdentifier(id);
    return this;
  }

  @Override
  public RemovalEdgeBuilder setType(String type) {
    this.type = type;
    return this;
  }

  @Override
  public boolean isComplete() {
    return this.id != null && this.type != null;
  }

  @Override
  public EdgeForRemoval build() {
    if (!this.isComplete()) {
      throw GraphBuilderException.becauseNodeIsMissingIdOrType();
    }
    return new EdgeForRemoval(this.id, this.type);
  }
}
