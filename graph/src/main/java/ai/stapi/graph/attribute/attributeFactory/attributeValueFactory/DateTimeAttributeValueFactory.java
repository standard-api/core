package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.attribute.attributeValue.DateTimeAttributeValue;
import java.time.Instant;


public class DateTimeAttributeValueFactory extends AbstractDateTimeAttributeValueFactory {

  @Override
  protected AttributeValue<?> createAttributeWithUtcTime(Instant utcTime) {
    return new DateTimeAttributeValue(utcTime);
  }

  @Override
  protected String getSupportedDataType() {
    return DateTimeAttributeValue.SERIALIZATION_TYPE;
  }
}
