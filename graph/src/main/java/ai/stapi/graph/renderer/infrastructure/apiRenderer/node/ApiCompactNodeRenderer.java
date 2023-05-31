package ai.stapi.graph.renderer.infrastructure.apiRenderer.node;

import ai.stapi.graph.renderer.infrastructure.apiRenderer.ApiRendererOptions;
import ai.stapi.graph.renderer.infrastructure.apiRenderer.attributes.ApiAttributesRenderer;
import ai.stapi.graph.renderer.infrastructure.apiRenderer.reponseGraph.CompactNodeResponse;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import org.springframework.beans.factory.annotation.Autowired;


public class ApiCompactNodeRenderer {

  private final ApiAttributesRenderer apiAttributesRenderer;
  private final ApiNodePrimaryNameFactory apiNodePrimaryNameFactory;

  @Autowired
  public ApiCompactNodeRenderer(
      ApiAttributesRenderer apiAttributesRenderer,
      ApiNodePrimaryNameFactory apiNodePrimaryNameFactory
  ) {
    this.apiAttributesRenderer = apiAttributesRenderer;
    this.apiNodePrimaryNameFactory = apiNodePrimaryNameFactory;
  }

  public CompactNodeResponse render(TraversableNode node) {
    return this.render(
        node,
        new ApiRendererOptions()
    );
  }

  public CompactNodeResponse render(TraversableNode node, RendererOptions options) {
    var attributes = this.apiAttributesRenderer.render(
        node,
        options
    );

    return new CompactNodeResponse(
        node.getId().toString(),
        node.getType(),
        this.apiNodePrimaryNameFactory.createNodePrimaryName(node),
        attributes
    );
  }
}
