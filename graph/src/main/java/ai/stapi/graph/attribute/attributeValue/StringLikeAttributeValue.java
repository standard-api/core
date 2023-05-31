package ai.stapi.graph.attribute.attributeValue;

import org.jetbrains.annotations.NotNull;

public interface StringLikeAttributeValue<T> extends AttributeValue<T> {
  
  String toStringValue();

  @Override
  default int compareTo(@NotNull AttributeValue<?> other) {
    if (other instanceof StringLikeAttributeValue<?> otherStringValue) {
      var originalThisValue = this.toStringValue();
      var originalOtherValue = otherStringValue.toStringValue();
      var lowercaseDiff = originalThisValue.toLowerCase().compareTo(originalOtherValue.toLowerCase());
      if (lowercaseDiff != 0) {
        return lowercaseDiff;
      }
      return originalThisValue.compareTo(originalOtherValue);
    } else {
      return 1;
    }
  }
}
