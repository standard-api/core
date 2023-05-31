package ai.stapi.graph.renderer.infrastructure.apiRenderer.reponseGraph;


import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class EdgeResponse implements Serializable {

  private final String edgeId;
  private final String type;
  private final CompactNodeResponse compactNodeFrom;
  private final CompactNodeResponse compactNodeTo;
  private final List<AttributeResponse> attributes;

  @JsonCreator
  public EdgeResponse(
      @NotNull String edgeId,
      @NotNull String type,
      @NotNull CompactNodeResponse compactNodeFrom,
      @NotNull CompactNodeResponse compactNodeTo,
      @NotNull List<AttributeResponse> attributes
  ) {
    this.edgeId = edgeId;
    this.type = type;
    this.compactNodeFrom = compactNodeFrom;
    this.compactNodeTo = compactNodeTo;
    this.attributes = attributes;
  }

  public String getEdgeId() {
    return edgeId;
  }

  public String getType() {
    return type;
  }

  public CompactNodeResponse getCompactNodeFrom() {
    return compactNodeFrom;
  }

  public CompactNodeResponse getCompactNodeTo() {
    return compactNodeTo;
  }

  public List<AttributeResponse> getAttributes() {
    return attributes;
  }

}
