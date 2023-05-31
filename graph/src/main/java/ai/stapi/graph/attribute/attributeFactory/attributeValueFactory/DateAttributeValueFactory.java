package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.attribute.attributeValue.DateAttributeValue;
import java.sql.Timestamp;


public class DateAttributeValueFactory extends AbstractDateTimeAttributeValueFactory {

  @Override
  protected AttributeValue<?> createAttributeWithTimeStamp(Timestamp timestamp) {
    return new DateAttributeValue(timestamp);
  }

  @Override
  protected String getSupportedDataType() {
    return DateAttributeValue.SERIALIZATION_TYPE;
  }
}
