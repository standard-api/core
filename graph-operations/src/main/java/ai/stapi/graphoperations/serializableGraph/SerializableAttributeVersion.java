package ai.stapi.graphoperations.serializableGraph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.ListAttribute;
import ai.stapi.graph.attribute.SetAttribute;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializableAttributeVersion {

  private final List<SerializableAttributeValue> values;
  private final Map<String, List<SerializableAttributeValue>> metaData;

  public static SerializableAttributeVersion fromAttribute(Attribute<?> attribute) {
    var metaData = new HashMap<String, List<SerializableAttributeValue>>();
    attribute.getMetaData().forEach((name, meta) -> metaData.put(
        name,
        meta.getValues().stream().map(SerializableAttributeValue::new).toList()
    ));
    if (attribute instanceof LeafAttribute<?, ?> leafAttribute) {
      return new SerializableAttributeVersion(
          List.of(new SerializableAttributeValue(leafAttribute.getBoxedValue())),
          metaData
      );
    }
    if (attribute instanceof ListAttribute listAttribute) {
      return new SerializableAttributeVersion(
          listAttribute.getBoxedValues().stream().map(SerializableAttributeValue::new).toList(),
          metaData
      );
    }
    if (attribute instanceof SetAttribute setAttribute) {
      return new SerializableAttributeVersion(
          setAttribute.getBoxedValues().stream().map(SerializableAttributeValue::new).toList(),
          metaData
      );
    }
    return new SerializableAttributeVersion(
        List.of(),
        Map.of()
    );
  }

  @JsonCreator
  public SerializableAttributeVersion(
      @JsonProperty("values") List<SerializableAttributeValue> values,
      @JsonProperty("metaData") Map<String, List<SerializableAttributeValue>> metaData
  ) {
    this.values = values;
    this.metaData = metaData;
  }

  public List<SerializableAttributeValue> getValues() {
    return values;
  }

  public Map<String, List<SerializableAttributeValue>> getMetaData() {
    return metaData;
  }
}
