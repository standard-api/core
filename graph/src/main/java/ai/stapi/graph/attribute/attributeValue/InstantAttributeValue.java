package ai.stapi.graph.attribute.attributeValue;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class InstantAttributeValue extends AbstractAttributeValue<Instant>
    implements StringLikeAttributeValue<Instant> {

  public static final String SERIALIZATION_TYPE = "instant";

  public InstantAttributeValue(Instant value) {
    super(value);
  }

  @Override
  public String getDataType() {
    return SERIALIZATION_TYPE;
  }

  @Override
  public String toStringValue() {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
    return this.getValue().atZone(ZoneOffset.UTC).format(formatter);
  }
}
