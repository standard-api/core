package ai.stapi.graphoperations.graphbuilder.specific.removal;

import ai.stapi.graphoperations.graphbuilder.exception.GraphBuilderException;
import ai.stapi.graph.graphElementForRemoval.NodeForRemoval;
import ai.stapi.identity.UniqueIdentifier;

public class RemovalNodeBuilder implements GraphElementForRemovalBuilder {

  private UniqueIdentifier id;
  private String type;

  @Override
  public RemovalNodeBuilder setId(UniqueIdentifier id) {
    this.id = id;
    return this;
  }

  public RemovalNodeBuilder setId(String id) {
    this.id = new UniqueIdentifier(id);
    return this;
  }

  @Override
  public RemovalNodeBuilder setType(String type) {
    this.type = type;
    return this;
  }

  @Override
  public boolean isComplete() {
    return this.id != null && this.type != null;
  }

  @Override
  public NodeForRemoval build() {
    if (!this.isComplete()) {
      throw GraphBuilderException.becauseNodeIsMissingIdOrType();
    }
    return new NodeForRemoval(this.id, this.type);
  }
}
