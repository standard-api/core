package ai.stapi.graph;

import ai.stapi.identity.UniqueIdentifier;

public class NodeInfo {

  private UniqueIdentifier id;
  private String type;
  private String name;

  public NodeInfo(UniqueIdentifier id, String type, String name) {
    this.id = id;
    this.type = type;
    this.name = name;
  }

  public String getId() {
    return id.toString();
  }

  public String getType() {
    return type;
  }

  public String getName() {
    return name;
  }
}
