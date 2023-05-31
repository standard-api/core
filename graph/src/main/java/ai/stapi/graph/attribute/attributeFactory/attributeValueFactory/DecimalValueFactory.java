package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeFactory.exceptions.CannotCreateAttribute;
import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.attribute.attributeValue.DecimalAttributeValue;
import org.apache.commons.lang3.math.NumberUtils;


public class DecimalValueFactory implements AttributeValueFactory {

  @Override
  public AttributeValue<?> create(Object value, String attributeName) {
    if (!this.isValidValue(value)) {
      throw CannotCreateAttribute.becauseProvidedValueCouldNotBeConvertedToProvidedDataType(
          attributeName,
          DecimalAttributeValue.SERIALIZATION_TYPE,
          value
      );
    }
    if (value instanceof Float floatValue) {
      return new DecimalAttributeValue(floatValue.doubleValue());
    }
    if (value instanceof Double doubleValue) {
      return new DecimalAttributeValue(doubleValue);
    }
    if (value instanceof Integer integerValue) {
      return new DecimalAttributeValue(integerValue.doubleValue());
    }
    if (value instanceof String stringValue) {
      return new DecimalAttributeValue(NumberUtils.createDouble(stringValue));
    }
    throw CannotCreateAttribute.becauseProvidedValueCouldNotBeConvertedToProvidedDataType(
        attributeName,
        DecimalAttributeValue.SERIALIZATION_TYPE,
        value
    );
  }

  @Override
  public boolean supportsDataType(String dataType) {
    return dataType.equals(DecimalAttributeValue.SERIALIZATION_TYPE);
  }

  @Override
  public boolean isValidValue(Object value) {
    if (value instanceof Float floatValue) {
      return true;
    }
    if (value instanceof Double doubleValue) {
      return true;
    }
    if (value instanceof Integer integerValue) {
      return true;
    }
    if (value instanceof String stringValue) {
      return NumberUtils.isParsable(stringValue);
    }
    return false;
  }
}
