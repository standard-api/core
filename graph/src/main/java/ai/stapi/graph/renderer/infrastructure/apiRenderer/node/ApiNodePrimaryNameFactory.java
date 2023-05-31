package ai.stapi.graph.renderer.infrastructure.apiRenderer.node;

import ai.stapi.graph.traversableGraphElements.TraversableNode;
import java.util.UUID;


public class ApiNodePrimaryNameFactory {

  public String createNodePrimaryName(TraversableNode node) {
    try {
      UUID.fromString(node.getId().toString());
    } catch (IllegalArgumentException e) {
      return node.getId().toString();
    }
    if (node.hasAttribute("name")) {
      return node.getAttribute("name").getValue().toString();
    }
    if (node.hasAttribute("path")) {
      return node.getAttribute("path").getValue().toString();
    }

    return node.getId().toString();
  }
}
