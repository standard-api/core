package ai.stapi.graph.renderer.infrastructure.responseRenderer.edge;

import ai.stapi.graph.renderer.infrastructure.responseRenderer.ResponseRendererOptions;
import ai.stapi.graph.renderer.infrastructure.responseRenderer.attributes.ResponseAttributesRenderer;
import ai.stapi.graph.renderer.infrastructure.responseRenderer.responseGraph.ResponseEdge;
import ai.stapi.graph.renderer.infrastructure.responseRenderer.responseGraph.ResponseNodeIdAndType;
import ai.stapi.graph.renderer.model.edgeRenderer.EdgeRenderer;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;
import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import org.springframework.beans.factory.annotation.Autowired;


public class ResponseEdgeRenderer implements EdgeRenderer {

  private final ResponseAttributesRenderer responseAttributesRenderer;

  @Autowired
  public ResponseEdgeRenderer(ResponseAttributesRenderer responseAttributesRenderer) {
    this.responseAttributesRenderer = responseAttributesRenderer;
  }

  @Override
  public ResponseEdge render(TraversableEdge edge) {
    return this.render(edge, new ResponseRendererOptions());
  }

  public ResponseEdge render(TraversableEdge edge, RendererOptions options) {

    var attributes = this.responseAttributesRenderer.render(
        edge,
        options
    );

    return new ResponseEdge(
        edge.getId().toString(),
        edge.getType(),
        new ResponseNodeIdAndType(edge.getNodeFromId().toString(), edge.getNodeFromType()),
        new ResponseNodeIdAndType(edge.getNodeFromId().toString(), edge.getNodeFromType()),
        attributes
    );
  }
}
