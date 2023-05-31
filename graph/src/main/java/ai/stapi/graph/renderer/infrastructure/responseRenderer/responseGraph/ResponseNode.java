package ai.stapi.graph.renderer.infrastructure.responseRenderer.responseGraph;

import ai.stapi.graph.renderer.model.RenderOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class ResponseNode implements RenderOutput, Comparable<ResponseNode> {

  private String uuid;
  private String type;
  private Map<String, ResponseAttribute<?>> attributes;

  private ResponseNode() {
  }

  public ResponseNode(
      String uuid,
      String type,
      Map<String, ResponseAttribute<?>> attributes
  ) {
    this.uuid = uuid;
    this.type = type;
    this.attributes = attributes;
  }

  public ResponseNode(String uuid, String type) {
    this(uuid, type, new HashMap<>());
  }

  @NotNull
  public String getUuid() {
    return uuid;
  }

  @NotNull
  public String getType() {
    return type;
  }

  @NotNull
  public Map<String, ResponseAttribute<?>> getAttributes() {
    return attributes;
  }


  @Override
  public String toString() {
    return "ResponseNode{"
        + "nodeId='" + uuid + '\''
        + ", type='" + type + '\''
        + ", attributes=" + attributes
        + '}';
  }

  @Override
  public String toPrintableString() {
    return this.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseNode that = (ResponseNode) o;
    return type.equals(that.type) && attributes.equals(that.attributes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, attributes);
  }

  @Override
  public int compareTo(@NotNull ResponseNode o) {
    return this.hashCode() - o.hashCode();
  }
}
