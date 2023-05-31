package ai.stapi.graph.attribute.attributeValue;

import org.jetbrains.annotations.NotNull;

public class BooleanAttributeValue extends AbstractAttributeValue<Boolean> {

  public static final String SERIALIZATION_TYPE = "boolean";

  public BooleanAttributeValue(Boolean value) {
    super(value);
  }

  @Override
  public String getDataType() {
    return SERIALIZATION_TYPE;
  }

  @Override
  public int compareTo(@NotNull AttributeValue<?> other) {
    if (other instanceof BooleanAttributeValue otherBooleanValue) {
      return this.getValue().compareTo(otherBooleanValue.getValue());
    } else {
      return -1;
    }
  }
}
