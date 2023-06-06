package ai.stapi.graphoperations.graphbuilder.specific.removal;

import ai.stapi.graph.graphElementForRemoval.GraphElementForRemoval;
import ai.stapi.identity.UniqueIdentifier;

public interface GraphElementForRemovalBuilder {

  GraphElementForRemovalBuilder setId(UniqueIdentifier id);

  GraphElementForRemovalBuilder setType(String type);

  boolean isComplete();

  GraphElementForRemoval build();
}
