package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.attribute.attributeValue.TimeAttributeValue;
import java.sql.Timestamp;


public class TimeAttributeValueFactory extends AbstractDateTimeAttributeValueFactory {

  @Override
  protected AttributeValue<?> createAttributeWithTimeStamp(Timestamp timestamp) {
    return new TimeAttributeValue(timestamp);
  }

  @Override
  protected String getSupportedDataType() {
    return TimeAttributeValue.SERIALIZATION_TYPE;
  }
}
