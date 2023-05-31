package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeFactory.exceptions.CannotCreateAttribute;
import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.attribute.attributeValue.PositiveIntegerAttributeValue;


public class PositiveIntegerValueFactory implements AttributeValueFactory {

  @Override
  public AttributeValue<?> create(Object value, String attributeName) {
    if (!this.isValidValue(value)) {
      throw CannotCreateAttribute.becauseProvidedValueCouldNotBeConvertedToProvidedDataType(
          attributeName,
          PositiveIntegerAttributeValue.SERIALIZATION_TYPE,
          value
      );
    }
    return new PositiveIntegerAttributeValue((Integer) value);
  }

  @Override
  public boolean supportsDataType(String dataType) {
    return dataType.equals(PositiveIntegerAttributeValue.SERIALIZATION_TYPE);
  }

  @Override
  public boolean isValidValue(Object value) {
    if (!(value instanceof Integer integerValue)) {
      return false;
    }
    return integerValue >= 0;
  }
}
