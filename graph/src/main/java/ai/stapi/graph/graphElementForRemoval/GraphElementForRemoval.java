package ai.stapi.graph.graphElementForRemoval;

import ai.stapi.identity.UniqueIdentifier;

public interface GraphElementForRemoval {

  UniqueIdentifier getGraphElementId();

  String getGraphElementType();

  String getGraphElementBaseType();
}
