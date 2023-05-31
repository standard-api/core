package ai.stapi.graph;

import java.util.Objects;
import java.util.UUID;

public class EdgeIdAndType {

  private String type;
  private UUID id;

  private EdgeIdAndType() {
  }

  public EdgeIdAndType(UUID id, String type) {
    this.type = type;
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public UUID getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EdgeIdAndType)) {
      return false;
    }
    EdgeIdAndType that = (EdgeIdAndType) o;
    return getType().equals(that.getType()) && getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getType(), getId());
  }
}
