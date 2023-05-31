package ai.stapi.graph.renderer.infrastructure.apiRenderer.edge;

import ai.stapi.graph.exceptions.NodeNotFound;
import ai.stapi.graph.renderer.infrastructure.apiRenderer.attributes.ApiAttributesRenderer;
import ai.stapi.graph.renderer.infrastructure.apiRenderer.node.ApiCompactNodeRenderer;
import ai.stapi.graph.renderer.infrastructure.apiRenderer.reponseGraph.EdgeResponse;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;
import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import org.springframework.beans.factory.annotation.Autowired;


public class ApiEdgeRenderer {

  private final ApiCompactNodeRenderer apiCompactNodeRenderer;
  private final ApiAttributesRenderer apiAttributesRenderer;
  
  @Autowired
  public ApiEdgeRenderer(
      ApiCompactNodeRenderer apiCompactNodeRenderer,
      ApiAttributesRenderer apiAttributesRenderer
  ) {
    this.apiCompactNodeRenderer = apiCompactNodeRenderer;
    this.apiAttributesRenderer = apiAttributesRenderer;
  }

  public EdgeResponse render(TraversableEdge edge, RendererOptions options) {
    TraversableNode nodeFrom;
    try {
      nodeFrom = edge.getNodeFrom();
    } catch (NodeNotFound e) {
      nodeFrom = edge.getNodeFrom();
    }
    var compactNodeFrom = this.apiCompactNodeRenderer.render(nodeFrom);

    TraversableNode nodeTo;
    try {
      nodeTo = edge.getNodeTo();
    } catch (NodeNotFound e) {
      nodeTo = edge.getNodeTo();
    }
    var compactNodeTo = this.apiCompactNodeRenderer.render(nodeTo);

    var attributes = this.apiAttributesRenderer.render(
        edge,
        options
    );

    return new EdgeResponse(
        edge.getId().toString(),
        edge.getType(),
        compactNodeFrom,
        compactNodeTo,
        attributes
    );
  }
}
