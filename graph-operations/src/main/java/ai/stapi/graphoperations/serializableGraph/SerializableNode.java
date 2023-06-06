package ai.stapi.graphoperations.serializableGraph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ai.stapi.graph.graphelements.Node;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializableNode extends SerializableGraphElement {

  public static SerializableNode fromInputNode(Node node) {
    var attributeMap = new HashMap<String, List<SerializableAttributeVersion>>();
    node.getVersionedAttributes()
        .getVersionedAttributeList()
        .forEach(attr -> attributeMap.put(
            attr.getName(),
            attr.streamAttributeVersions().map(SerializableAttributeVersion::fromAttribute).toList()
        ));

    return new SerializableNode(
        node.getId().getId(),
        node.getType(),
        attributeMap
    );
  }

  public SerializableNode(
      String id,
      String type,
      Map<String, List<SerializableAttributeVersion>> attributes
  ) {
    super(id, type, attributes);
  }

  @JsonCreator
  private SerializableNode(
      @JsonProperty(GraphElementKeys.ID) String reference,

      @JsonProperty(GraphElementKeys.ATTRIBUTES)
      Map<String, List<SerializableAttributeVersion>> attributes,

      @JsonProperty(GraphElementKeys.KEY) String key,
      @JsonProperty(GraphElementKeys.REV) String revision
  ) {
    super(reference, attributes, key, revision);
  }
}
