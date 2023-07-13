package ai.stapi.graph.exceptions;

import ai.stapi.identity.UniqueIdentifier;

public class NodeWithSameIdAndTypeAlreadyExists extends GraphException {

  public NodeWithSameIdAndTypeAlreadyExists(UniqueIdentifier uuid, String type) {
    super(
        "Node can not be saved, because node with same id and type already exists." +
            "You can use replace instead." +
            "\nUUID: " + uuid.toString() +
            "\nNode Type: " + type
    );
  }
}
