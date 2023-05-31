package ai.stapi.graph;

import ai.stapi.identity.UniqueIdentifier;
import java.util.Objects;

public class NodeIdAndType {

  private String type;
  private UniqueIdentifier id;

  public static NodeIdAndType fromString(String fullId) {
    var split = fullId.split("/");
    return new NodeIdAndType(
        new UniqueIdentifier(split[1]),
        split[0]
    );
  }

  public NodeIdAndType(UniqueIdentifier id, String type) {
    this.type = type;
    this.id = id;
  }

  private NodeIdAndType() {
  }

  public String getType() {
    return type;
  }

  public UniqueIdentifier getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof NodeIdAndType)) {
      return false;
    }
    NodeIdAndType that = (NodeIdAndType) o;
    return getType().equals(that.getType()) && getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getType(), getId());
  }

  @Override
  public String toString() {
    return String.format("%s/%s", this.type, this.id.getId());
  }
}
