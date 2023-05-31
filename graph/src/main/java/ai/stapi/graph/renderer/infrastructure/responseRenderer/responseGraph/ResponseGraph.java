package ai.stapi.graph.renderer.infrastructure.responseRenderer.responseGraph;

import ai.stapi.graph.renderer.model.GraphRenderOutput;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class ResponseGraph implements GraphRenderOutput {

  private final Map<String, ResponseNode> nodes;

  private final Map<String, ResponseEdge> edges;

  public ResponseGraph() {
    this.nodes = new HashMap<>();
    this.edges = new HashMap<>();
  }

  public ResponseGraph(
      Map<String, ResponseNode> nodes,
      Map<String, ResponseEdge> edges
  ) {
    this.nodes = nodes;
    this.edges = edges;
  }

  @Override
  public String toString() {
    return "ResponseGraph{"
        + "nodes=" + nodes
        + ", edges=" + edges
        + '}';
  }

  @Override
  public String toPrintableString() {
    return this.toString();
  }

  @NotNull
  public Map<String, ResponseNode> getNodes() {
    return nodes;
  }

  @NotNull
  public Map<String, ResponseEdge> getEdges() {
    return edges;
  }
}
