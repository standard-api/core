package ai.stapi.graph.attribute.attributeValue;

import java.time.LocalTime;

public class TimeAttributeValue extends AbstractAttributeValue<LocalTime>
    implements StringLikeAttributeValue<LocalTime> {

  public static final String SERIALIZATION_TYPE = "time";

  public TimeAttributeValue(LocalTime value) {
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
