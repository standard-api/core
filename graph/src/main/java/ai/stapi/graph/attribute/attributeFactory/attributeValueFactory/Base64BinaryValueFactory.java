package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeFactory.exceptions.CannotCreateAttribute;
import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.attribute.attributeValue.Base64BinaryAttributeValue;


public class Base64BinaryValueFactory implements AttributeValueFactory {

  @Override
  public AttributeValue<?> create(Object value, String attributeName) {
    if (!this.isValidValue(value)) {
      throw CannotCreateAttribute.becauseProvidedValueCouldNotBeConvertedToProvidedDataType(
          attributeName,
          Base64BinaryAttributeValue.SERIALIZATION_TYPE,
          value
      );
    }
    return new Base64BinaryAttributeValue((String) value);
  }

  @Override
  public boolean supportsDataType(String dataType) {
    return dataType.equals(Base64BinaryAttributeValue.SERIALIZATION_TYPE);
  }

  @Override
  public boolean isValidValue(Object value) {
    return value instanceof String;
  }
}
