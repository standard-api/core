package ai.stapi.graph.renderer.infrastructure.responseRenderer.node;

import ai.stapi.graph.renderer.infrastructure.responseRenderer.ResponseRendererOptions;
import ai.stapi.graph.renderer.infrastructure.responseRenderer.attributes.ResponseAttributesRenderer;
import ai.stapi.graph.renderer.infrastructure.responseRenderer.responseGraph.ResponseNode;
import ai.stapi.graph.renderer.model.nodeRenderer.NodeRenderer;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import org.springframework.beans.factory.annotation.Autowired;



public class ResponseNodeRenderer implements NodeRenderer {

  private final ResponseAttributesRenderer responseAttributesRenderer;

  @Autowired
  public ResponseNodeRenderer(ResponseAttributesRenderer responseAttributesRenderer) {
    this.responseAttributesRenderer = responseAttributesRenderer;
  }

  @Override
  public boolean supports(RendererOptions options) {
    return options.getClass().equals(ResponseRendererOptions.class);
  }

  @Override
  public ResponseNode render(TraversableNode node) {
    return this.render(
        node,
        new ResponseRendererOptions()
    );
  }

  @Override
  public ResponseNode render(TraversableNode node, RendererOptions options) {
    var attributes = this.responseAttributesRenderer.render(
        node,
        options
    );

    return new ResponseNode(
        node.getId().toString(),
        node.getType(),
        attributes
    );
  }
}
