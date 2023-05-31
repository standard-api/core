package ai.stapi.graph.renderer.infrastructure.responseRenderer.responseGraph;

import ai.stapi.graph.renderer.model.RenderOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class ResponseEdge implements RenderOutput, Comparable<ResponseEdge> {

  private String uuid;
  private String type;
  private ResponseNodeIdAndType nodeFrom;
  private ResponseNodeIdAndType nodeTo;
  private Map<String, ResponseAttribute<?>> attributes;

  private ResponseEdge() {
  }

  public ResponseEdge(
      String uuid,
      String type,
      ResponseNodeIdAndType nodeFrom,
      ResponseNodeIdAndType nodeTo,
      Map<String, ResponseAttribute<?>> attributes
  ) {
    this.uuid = uuid;
    this.type = type;
    this.nodeFrom = nodeFrom;
    this.nodeTo = nodeTo;
    this.attributes = attributes;
  }

  public ResponseEdge(
      String uuid,
      String type,
      ResponseNodeIdAndType nodeFrom,
      ResponseNodeIdAndType nodeTo
  ) {
    this(uuid, type, nodeFrom, nodeTo, new HashMap<>());
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
  public ResponseNodeIdAndType getNodeFrom() {
    return nodeFrom;
  }

  @NotNull
  public ResponseNodeIdAndType getNodeTo() {
    return nodeTo;
  }

  @NotNull
  public Map<String, ResponseAttribute<?>> getAttributes() {
    return attributes;
  }

  @Override
  public String toString() {
    return "ResponseEdge{"
        + "edgeId='" + uuid + '\''
        + ", type='" + type + '\''
        + ", nodeFrom=" + nodeFrom
        + ", nodeTo=" + nodeTo
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
    ResponseEdge that = (ResponseEdge) o;
    return type.equals(that.type) && nodeFrom.equals(that.nodeFrom) && nodeTo.equals(that.nodeTo)
        && attributes.equals(that.attributes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, nodeFrom, nodeTo, attributes);
  }

  @Override
  public int compareTo(@NotNull ResponseEdge o) {
    return this.hashCode() - o.hashCode();
  }
}
