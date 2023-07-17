package ai.stapi.graph.attribute.attributeValue;

import java.time.Instant;

public class DateTimeAttributeValue extends AbstractAttributeValue<Instant>
    implements StringLikeAttributeValue<Instant> {

  public static final String SERIALIZATION_TYPE = "dateTime";

  public DateTimeAttributeValue(Instant value) {
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
