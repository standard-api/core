package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.attribute.attributeValue.InstantAttributeValue;
import java.sql.Timestamp;


public class InstantAttributeValueFactory extends AbstractDateTimeAttributeValueFactory {

  @Override
  protected AttributeValue<?> createAttributeWithTimeStamp(Timestamp timestamp) {
    return new InstantAttributeValue(timestamp);
  }

  @Override
  protected String getSupportedDataType() {
    return InstantAttributeValue.SERIALIZATION_TYPE;
  }
}
