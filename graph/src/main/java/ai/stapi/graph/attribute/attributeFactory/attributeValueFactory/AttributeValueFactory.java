package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;

public interface AttributeValueFactory {

  AttributeValue<?> create(Object value, String attributeName);

  boolean supportsDataType(String dataType);

  boolean isValidValue(Object value);
}
