package ai.stapi.graph.attribute.attributeValue;

import java.sql.Timestamp;

public class DateAttributeValue extends AbstractAttributeValue<Timestamp> implements StringLikeAttributeValue<Timestamp> {

  public static final String SERIALIZATION_TYPE = "date";

  public DateAttributeValue(Timestamp value) {
    super(value);
  }

  @Override
  public String getDataType() {
    return SERIALIZATION_TYPE;
  }

  @Override
  public String toStringValue() {
    return this.getValue().toString();
  }
}
