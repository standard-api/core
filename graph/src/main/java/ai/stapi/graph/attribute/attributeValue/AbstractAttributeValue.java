package ai.stapi.graph.attribute.attributeValue;

import java.util.Objects;

public abstract class AbstractAttributeValue<T> implements AttributeValue<T> {

  private final T value;

  protected AbstractAttributeValue(T value) {
    this.value = value;
  }
  

  @Override
  public T getValue() {
    return value;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof AbstractAttributeValue<?> otherAttributeValue)) {
      return false;
    }
    return this.getValue().equals(otherAttributeValue.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getValue());
  }
}
