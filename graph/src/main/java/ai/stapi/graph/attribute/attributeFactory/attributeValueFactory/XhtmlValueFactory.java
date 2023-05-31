package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeFactory.exceptions.CannotCreateAttribute;
import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.attribute.attributeValue.XhtmlAttributeValue;


public class XhtmlValueFactory implements AttributeValueFactory {

  @Override
  public AttributeValue<?> create(Object value, String attributeName) {
    if (!this.isValidValue(value)) {
      throw CannotCreateAttribute.becauseProvidedValueCouldNotBeConvertedToProvidedDataType(
          attributeName,
          XhtmlAttributeValue.SERIALIZATION_TYPE,
          value
      );
    }
    return new XhtmlAttributeValue((String) value);
  }

  @Override
  public boolean supportsDataType(String dataType) {
    return dataType.equals(XhtmlAttributeValue.SERIALIZATION_TYPE);
  }

  @Override
  public boolean isValidValue(Object value) {
    return value instanceof String;
  }
}
