package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeFactory.exceptions.CannotCreateAttribute;
import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.attribute.attributeValue.OidAttributeValue;


public class OidValueFactory implements AttributeValueFactory {

  @Override
  public AttributeValue<?> create(Object value, String attributeName) {
    if (!this.isValidValue(value)) {
      throw CannotCreateAttribute.becauseProvidedValueCouldNotBeConvertedToProvidedDataType(
          attributeName,
          OidAttributeValue.SERIALIZATION_TYPE,
          value
      );
    }
    return new OidAttributeValue((String) value);
  }

  @Override
  public boolean supportsDataType(String dataType) {
    return dataType.equals(OidAttributeValue.SERIALIZATION_TYPE);
  }

  @Override
  public boolean isValidValue(Object value) {
    return value instanceof String;
  }
}
