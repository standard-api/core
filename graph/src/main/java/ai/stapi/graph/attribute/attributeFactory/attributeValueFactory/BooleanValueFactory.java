package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeFactory.exceptions.CannotCreateAttribute;
import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;


public class BooleanValueFactory implements AttributeValueFactory {

  @Override
  public AttributeValue<?> create(Object value, String attributeName) {
    if (!this.isValidValue(value)) {
      throw CannotCreateAttribute.becauseProvidedValueCouldNotBeConvertedToProvidedDataType(
          attributeName,
          BooleanAttributeValue.SERIALIZATION_TYPE,
          value
      );
    }
    return new BooleanAttributeValue((Boolean) value);
  }

  @Override
  public boolean supportsDataType(String dataType) {
    return dataType.equals(BooleanAttributeValue.SERIALIZATION_TYPE);
  }

  @Override
  public boolean isValidValue(Object value) {
    return value instanceof Boolean;
  }
}
