package ai.stapi.graph.attribute.attributeValue;

import org.jetbrains.annotations.NotNull;

public interface NumberLikeAttributeValue<T> extends AttributeValue<T> {
  
  Double toDoubleValue();

  @Override
  default int compareTo(@NotNull AttributeValue<?> other) {
    if (other instanceof BooleanAttributeValue) {
      return 1;
    }
    if (other instanceof NumberLikeAttributeValue<?> otherNumberValue) {
      return this.toDoubleValue().compareTo(otherNumberValue.toDoubleValue());
    }
    return -1;
  }
}
