package ai.stapi.graph.renderer.infrastructure.responseRenderer.responseGraph;

import java.util.Objects;

public class ResponseEdgeIdAndType {

  private String edgeId;

  private String edgeType;

  private ResponseEdgeIdAndType() {
  }

  public ResponseEdgeIdAndType(String edgeId, String edgeType) {
    this.edgeId = edgeId;
    this.edgeType = edgeType;
  }

  public String getEdgeId() {
    return edgeId;
  }

  public String getEdgeType() {
    return edgeType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseEdgeIdAndType that = (ResponseEdgeIdAndType) o;
    return edgeType.equals(that.edgeType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(edgeType);
  }
}
