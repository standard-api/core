package ai.stapi.graph.renderer.infrastructure.responseRenderer.responseGraph;

import ai.stapi.graph.attribute.Attribute;
import java.sql.Timestamp;
import java.util.Objects;

public class ResponseAttributeVersion<T> {

  private T value;

  private Timestamp createdAt;

  private ResponseAttributeVersion() {
  }

  public ResponseAttributeVersion(Attribute<T> attribute) {
    this.value = attribute.getValue();
    this.createdAt = attribute.getCreatedAt();
  }

  public T getValue() {
    return value;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseAttributeVersion<?> that = (ResponseAttributeVersion<?>) o;
    return value.equals(that.value) && createdAt.equals(that.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, createdAt);
  }
}
