package ai.stapi.graph.renderer.infrastructure.apiRenderer.node;

import ai.stapi.graph.renderer.infrastructure.apiRenderer.ApiRendererOptions;
import ai.stapi.graph.renderer.infrastructure.apiRenderer.attributes.ApiAttributesRenderer;
import ai.stapi.graph.renderer.infrastructure.apiRenderer.edge.ApiEdgeRenderer;
import ai.stapi.graph.renderer.infrastructure.apiRenderer.reponseGraph.EdgeResponse;
import ai.stapi.graph.renderer.infrastructure.apiRenderer.reponseGraph.NodeResponse;
import ai.stapi.graph.renderer.model.RenderOutput;
import ai.stapi.graph.renderer.model.nodeRenderer.NodeRenderer;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;


public class ApiNodeRenderer implements NodeRenderer {

  private final ApiAttributesRenderer apiAttributesRenderer;
  private final ApiEdgeRenderer apiEdgeRenderer;
  private final ApiNodePrimaryNameFactory apiNodePrimaryNameFactory;

  @Autowired
  public ApiNodeRenderer(
      ApiAttributesRenderer apiAttributesRenderer,
      ApiEdgeRenderer apiEdgeRenderer,
      ApiNodePrimaryNameFactory apiNodePrimaryNameFactory
  ) {
    this.apiAttributesRenderer = apiAttributesRenderer;
    this.apiEdgeRenderer = apiEdgeRenderer;
    this.apiNodePrimaryNameFactory = apiNodePrimaryNameFactory;
  }

  @Override
  public boolean supports(RendererOptions options) {
    return options.getClass().equals(ApiRendererOptions.class);
  }

  @Override
  public RenderOutput render(TraversableNode node) {
    return this.render(
        node,
        new ApiRendererOptions()
    );
  }

  @Override
  public RenderOutput render(TraversableNode node, RendererOptions options) {
    var attributes = this.apiAttributesRenderer.render(
        node,
        options
    );

    var edges = new ArrayList<EdgeResponse>();
    node.getEdges().forEach(
        edge -> {
          edges.add(this.apiEdgeRenderer.render(
              edge,
              options
          ));
        }
    );

    return new NodeResponse(
        node.getId().toString(),
        node.getType(),
        this.apiNodePrimaryNameFactory.createNodePrimaryName(node),
        attributes,
        edges
    );
  }
}
