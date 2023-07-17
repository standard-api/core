package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.attribute.attributeValue.InstantAttributeValue;
import java.time.Instant;


public class InstantAttributeValueFactory extends AbstractDateTimeAttributeValueFactory {

  @Override
  protected AttributeValue<?> createAttributeWithUtcTime(Instant utcTime) {
    return new InstantAttributeValue(utcTime);
  }

  @Override
  protected String getSupportedDataType() {
    return InstantAttributeValue.SERIALIZATION_TYPE;
  }
}
