package ai.stapi.graph.attribute;

import ai.stapi.graph.attribute.attributeValue.InstantAttributeValue;
import java.time.Instant;
import java.util.List;

public class CreatedAtMetaData extends MetaData {

  public static final String NAME = "createdAt";

  public static CreatedAtMetaData fromMetaData(MetaData metaData) {
    if (metaData instanceof CreatedAtMetaData createdAtMetaData) {
      return createdAtMetaData;
    }
    return new CreatedAtMetaData((InstantAttributeValue) metaData.getValues().get(0));
  }

  public CreatedAtMetaData(InstantAttributeValue instantAttribute) {
    super(NAME, List.of(instantAttribute));
  }

  public CreatedAtMetaData(Instant utcTime) {
    super(NAME, List.of(new InstantAttributeValue(utcTime)));
  }

  public CreatedAtMetaData(String utcTime) {
    super(NAME, List.of(new InstantAttributeValue(Instant.parse(utcTime))));
  }

  public Instant getInstant() {
    return (Instant) this.getValues().get(0).getValue();
  }
}
