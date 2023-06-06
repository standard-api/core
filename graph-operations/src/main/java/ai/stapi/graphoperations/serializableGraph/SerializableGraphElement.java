package ai.stapi.graphoperations.serializableGraph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public abstract class SerializableGraphElement {

  private final String id;
  private final String type;
  private final Map<String, List<SerializableAttributeVersion>> attributes;

  protected SerializableGraphElement(
      String id,
      String type,
      Map<String, List<SerializableAttributeVersion>> attributes
  ) {
    this.id = id;
    this.type = type;
    this.attributes = attributes;
  }

  @JsonCreator
  protected SerializableGraphElement(
      @JsonProperty(GraphElementKeys.ID) String reference,
      @JsonProperty(GraphElementKeys.ATTRIBUTES)
      Map<String, List<SerializableAttributeVersion>> attributes,
      @JsonProperty(GraphElementKeys.KEY) String key,
      @JsonProperty(GraphElementKeys.REV) String revision
  ) {
    var split = reference.split("/");
    this.id = split[1];
    this.type = split[0];
    this.attributes = attributes;
  }

  @JsonProperty(GraphElementKeys.ID)
  public String getGlobalId() {
    return String.format("%s/%s", this.getType(), this.getId());
  }

  @JsonProperty(GraphElementKeys.KEY)
  public String getId() {
    return id;
  }

  @JsonIgnore
  public String getType() {
    return type;
  }

  public Map<String, List<SerializableAttributeVersion>> getAttributes() {
    return attributes;
  }
}
