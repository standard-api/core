package ai.stapi.graph.exceptions;

import ai.stapi.identity.UniqueIdentifier;

public class NodeWithSameIdAlreadyExists extends GraphException {

  public NodeWithSameIdAlreadyExists(UniqueIdentifier uuid, String type) {
    super(
        "Node can not be saved, because node with same id already exists." +
            "You can use replace instead." +
            "\nUUID: " + uuid.toString() +
            "\nNode Type: " + type
    );
  }
}
