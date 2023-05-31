package ai.stapi.graph.traversableGraphElements;

import ai.stapi.graph.AttributeContainer;
import ai.stapi.identity.UniqueIdentifier;

public interface TraversableGraphElement extends AttributeContainer {

  UniqueIdentifier getId();

  String getType();
}
