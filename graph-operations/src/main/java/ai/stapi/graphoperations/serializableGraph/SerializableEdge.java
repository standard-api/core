package ai.stapi.graphoperations.serializableGraph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ai.stapi.graph.NodeIdAndType;
import ai.stapi.graph.graphelements.Edge;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializableEdge extends SerializableGraphElement {

  private final NodeIdAndType nodeFrom;
  private final NodeIdAndType nodeTo;

  public static SerializableEdge fromInputEdge(Edge edge) {
    var attributeMap = new HashMap<String, List<SerializableAttributeVersion>>();
    edge.getVersionedAttributes()
        .getVersionedAttributeList()
        .forEach(attr -> attributeMap.put(
            attr.getName(),
            attr.streamAttributeVersions().map(SerializableAttributeVersion::fromAttribute).toList()
        ));

    return new SerializableEdge(
        edge.getId().getId(),
        edge.getType(),
        attributeMap,
        edge.getNodeFromIdAndType(),
        edge.getNodeToIdAndType()
    );
  }

  public SerializableEdge(
      String id,
      String type,
      Map<String, List<SerializableAttributeVersion>> attributes,
      NodeIdAndType nodeFrom,
      NodeIdAndType nodeTo
  ) {
    super(id, type, attributes);
    this.nodeFrom = nodeFrom;
    this.nodeTo = nodeTo;
  }

  @JsonCreator
  private SerializableEdge(
      @JsonProperty(GraphElementKeys.ID) String reference,

      @JsonProperty(GraphElementKeys.ATTRIBUTES)
      Map<String, List<SerializableAttributeVersion>> attributes,

      @JsonProperty(GraphElementKeys.FROM) String from,
      @JsonProperty(GraphElementKeys.TO) String to,
      @JsonProperty(GraphElementKeys.REV) String revision,
      @JsonProperty(GraphElementKeys.KEY) String key
  ) {
    super(reference, attributes, key, revision);
    this.nodeFrom = NodeIdAndType.fromString(from);
    this.nodeTo = NodeIdAndType.fromString(to);
  }

  @JsonProperty(GraphElementKeys.FROM)
  public String getFrom() {
    return this.getNodeFrom().toString();
  }

  @JsonProperty(GraphElementKeys.TO)
  public String getTo() {
    return this.getNodeTo().toString();
  }

  @JsonIgnore
  public NodeIdAndType getNodeFrom() {
    return nodeFrom;
  }

  @JsonIgnore
  public NodeIdAndType getNodeTo() {
    return nodeTo;
  }
}
