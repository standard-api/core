package ai.stapi.graph.renderer.infrastructure.apiRenderer.reponseGraph;

import ai.stapi.graph.renderer.model.RenderOutput;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class NodeResponse implements RenderOutput, Serializable {

  private final String nodeId;
  private final String type;
  private final String primaryName;
  private final List<AttributeResponse> attributes;
  private final List<EdgeResponse> edges;


  public NodeResponse(String nodeId, String type, String primaryName) {
    this.nodeId = nodeId;
    this.type = type;
    this.primaryName = primaryName;
    this.attributes = new ArrayList<>();
    this.edges = new ArrayList<>();
  }

  public NodeResponse(
      @NotNull String nodeId,
      @NotNull String type,
      @NotNull String primaryName,
      @NotNull List<AttributeResponse> attributes
  ) {
    this.nodeId = nodeId;
    this.type = type;
    this.primaryName = primaryName;
    this.attributes = attributes;
    this.edges = new ArrayList<>();
  }

  @JsonCreator
  public NodeResponse(
      @NotNull String nodeId,
      @NotNull String type,
      @NotNull String primaryName,
      List<AttributeResponse> attributes,
      @NotNull List<EdgeResponse> edges
  ) {
    this.nodeId = nodeId;
    this.type = type;
    this.primaryName = primaryName;
    this.attributes = attributes.stream().sorted(
        Comparator.comparing(AttributeResponse::getType)
    ).collect(Collectors.toList());
    this.edges = edges;
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

  public List<EdgeResponse> getEdges() {
    return edges;
  }

  public String getNodeId() {
    return nodeId;
  }

  @Override
  public String toPrintableString() {
    return "ApiNodeRender{"
        + "nodeId='" + nodeId + '\''
        + ", type='" + type + '\''
        + ", primaryName='" + primaryName + '\''
        + ", attributes=" + attributes
        + ", edges=" + edges
        + '}';
  }
}
