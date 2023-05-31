package ai.stapi.graph.attribute;

import ai.stapi.graph.attribute.attributeValue.InstantAttributeValue;
import java.sql.Timestamp;
import java.util.List;

public class CreatedAtMetaData extends MetaData {

  public static final String NAME = "createdAt";

  public static CreatedAtMetaData fromMetaData(MetaData metaData) {
    if (metaData instanceof CreatedAtMetaData createdAtMetaData) {
      return createdAtMetaData;
    }
    return new CreatedAtMetaData((InstantAttributeValue) metaData.getValues().get(0));
  }

  public CreatedAtMetaData(InstantAttributeValue timestamp) {
    super(NAME, List.of(timestamp));
  }

  public CreatedAtMetaData(Timestamp timestamp) {
    super(NAME, List.of(new InstantAttributeValue(timestamp)));
  }

  public CreatedAtMetaData(String timestamp) {
    super(NAME, List.of(new InstantAttributeValue(Timestamp.valueOf(timestamp))));
  }

  public Timestamp getTimestamp() {
    return (Timestamp) this.getValues().get(0).getValue();
  }
}
