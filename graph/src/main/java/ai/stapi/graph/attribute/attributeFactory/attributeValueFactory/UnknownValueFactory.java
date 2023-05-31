package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.attribute.attributeValue.UnknownAttributeValue;


public class UnknownValueFactory implements AttributeValueFactory {

  @Override
  public AttributeValue<?> create(Object value, String attributeName) {
    return new UnknownAttributeValue(value);
  }

  @Override
  public boolean supportsDataType(String dataType) {
    return dataType.equals(UnknownAttributeValue.SERIALIZATION_TYPE);
  }

  @Override
  public boolean isValidValue(Object value) {
    return true;
  }
}
