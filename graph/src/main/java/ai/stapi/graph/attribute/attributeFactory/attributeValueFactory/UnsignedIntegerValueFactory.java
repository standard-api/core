package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeFactory.exceptions.CannotCreateAttribute;
import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.attribute.attributeValue.UnsignedIntegerAttributeValue;


public class UnsignedIntegerValueFactory implements AttributeValueFactory {

  @Override
  public AttributeValue<?> create(Object value, String attributeName) {
    if (!this.isValidValue(value)) {
      throw CannotCreateAttribute.becauseProvidedValueCouldNotBeConvertedToProvidedDataType(
          attributeName,
          UnsignedIntegerAttributeValue.SERIALIZATION_TYPE,
          value
      );
    }
    return new UnsignedIntegerAttributeValue((Integer) value);
  }

  @Override
  public boolean supportsDataType(String dataType) {
    return dataType.equals(UnsignedIntegerAttributeValue.SERIALIZATION_TYPE);
  }

  @Override
  public boolean isValidValue(Object value) {
    return value instanceof Integer;
  }
}
