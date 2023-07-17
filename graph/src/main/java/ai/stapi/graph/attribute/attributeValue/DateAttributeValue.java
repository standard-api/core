package ai.stapi.graph.attribute.attributeValue;

import java.time.LocalDate;

public class DateAttributeValue extends AbstractAttributeValue<LocalDate>
    implements StringLikeAttributeValue<LocalDate> {

  public static final String SERIALIZATION_TYPE = "date";

  public DateAttributeValue(LocalDate date) {
    super(date);
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
