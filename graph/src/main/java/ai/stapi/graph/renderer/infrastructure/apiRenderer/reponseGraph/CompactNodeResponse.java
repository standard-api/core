package ai.stapi.graph.renderer.infrastructure.apiRenderer.reponseGraph;


import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class CompactNodeResponse implements Serializable {

  private final String nodeId;
  private final String type;
  private final String primaryName;
  private final List<AttributeResponse> attributes;

  @JsonCreator
  public CompactNodeResponse(
      @NotNull String nodeId,
      @NotNull String type,
      @NotNull String primaryName,
      @NotNull List<AttributeResponse> attributes
  ) {
    this.nodeId = nodeId;
    this.type = type;
    this.primaryName = primaryName;
    this.attributes = attributes;
  }

  public String getType() {
    return type;
  }

  public String getPrimaryName() {
    return primaryName;
  }

  public List<AttributeResponse> getAttributes() {
    return attributes;
  }

  public String getNodeId() {
    return nodeId;
  }

  public String toString() {
    if (!this.type.equals(this.primaryName)) {
      return this.getPrimaryName() + " (" + this.getType() + ")";
    }
    return this.getType();
  }
}
