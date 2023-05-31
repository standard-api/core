package ai.stapi.graph.inputGraphElements;

import ai.stapi.identity.UniqueIdentifier;
import java.io.Serializable;

public interface GraphElement extends Serializable {

  String getType();

  UniqueIdentifier getId();
}
