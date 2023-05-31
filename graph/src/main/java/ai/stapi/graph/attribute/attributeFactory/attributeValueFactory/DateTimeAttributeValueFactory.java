package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.attribute.attributeValue.DateTimeAttributeValue;
import java.sql.Timestamp;


public class DateTimeAttributeValueFactory extends AbstractDateTimeAttributeValueFactory {

  @Override
  protected AttributeValue<?> createAttributeWithTimeStamp(Timestamp timestamp) {
    return new DateTimeAttributeValue(timestamp);
  }

  @Override
  protected String getSupportedDataType() {
    return DateTimeAttributeValue.SERIALIZATION_TYPE;
  }
}
