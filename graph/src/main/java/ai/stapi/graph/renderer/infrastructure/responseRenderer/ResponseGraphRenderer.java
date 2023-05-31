package ai.stapi.graph.renderer.infrastructure.responseRenderer;

import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.renderer.infrastructure.responseRenderer.edge.ResponseEdgeRenderer;
import ai.stapi.graph.renderer.infrastructure.responseRenderer.node.ResponseNodeRenderer;
import ai.stapi.graph.renderer.infrastructure.responseRenderer.responseGraph.ResponseEdge;
import ai.stapi.graph.renderer.infrastructure.responseRenderer.responseGraph.ResponseGraph;
import ai.stapi.graph.renderer.infrastructure.responseRenderer.responseGraph.ResponseNode;
import ai.stapi.graph.renderer.model.GraphRenderer;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;
import java.util.Comparator;
import java.util.LinkedHashMap;


public class ResponseGraphRenderer implements GraphRenderer {

  private final ResponseNodeRenderer responseNodeRenderer;
  private final ResponseEdgeRenderer responseEdgeRenderer;

  public ResponseGraphRenderer(
      ResponseNodeRenderer responseNodeRenderer,
      ResponseEdgeRenderer responseEdgeRenderer
  ) {
    this.responseNodeRenderer = responseNodeRenderer;
    this.responseEdgeRenderer = responseEdgeRenderer;
  }

  @Override
  public boolean supports(RendererOptions options) {
    return options instanceof ResponseRendererOptions;
  }

  @Override
  public ResponseGraph render(InMemoryGraphRepository graph) {
    return this.render(graph, new ResponseRendererOptions());
  }

  @Override
  public ResponseGraph render(InMemoryGraphRepository graph,
      RendererOptions options) {
    var nodes = new LinkedHashMap<String, ResponseNode>();
    var edges = new LinkedHashMap<String, ResponseEdge>();

    graph.loadAllNodes().stream()
        .sorted(Comparator.comparing(
            node -> node.getSortingNameWithNodeTypeFallback() + node.getIdlessHashCodeWithEdges()))
        .map(traversableNode -> this.responseNodeRenderer.render(traversableNode, options))
        .forEach(responseNode -> nodes.put(responseNode.getUuid(), responseNode));

    graph.loadAllEdges().stream()
        .map(traversableEdge -> this.responseEdgeRenderer.render(traversableEdge, options))
        .sorted()
        .forEach(responseEdge -> edges.put(responseEdge.getUuid(), responseEdge));
    return new ResponseGraph(nodes, edges);
  }
}
