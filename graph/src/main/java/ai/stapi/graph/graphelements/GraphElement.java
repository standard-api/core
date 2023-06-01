package ai.stapi.graph.graphelements;

import ai.stapi.identity.UniqueIdentifier;
import java.io.Serializable;

public interface GraphElement extends Serializable {

  String getType();

  UniqueIdentifier getId();
}
