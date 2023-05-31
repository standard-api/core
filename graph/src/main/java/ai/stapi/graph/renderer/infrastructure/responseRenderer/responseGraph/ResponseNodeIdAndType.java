package ai.stapi.graph.renderer.infrastructure.responseRenderer.responseGraph;


import java.util.Objects;

public class ResponseNodeIdAndType {

  private String nodeId;

  private String nodeType;

  private ResponseNodeIdAndType() {
  }

  public ResponseNodeIdAndType(String nodeId, String nodeType) {
    this.nodeId = nodeId;
    this.nodeType = nodeType;
  }

  public String getNodeId() {
    return nodeId;
  }

  public String getNodeType() {
    return nodeType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseNodeIdAndType that = (ResponseNodeIdAndType) o;
    return nodeType.equals(that.nodeType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeType);
  }
}
