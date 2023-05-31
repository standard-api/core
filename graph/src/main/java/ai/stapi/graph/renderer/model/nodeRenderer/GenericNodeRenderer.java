package ai.stapi.graph.renderer.model.nodeRenderer;

import ai.stapi.graph.renderer.model.RenderOutput;
import ai.stapi.graph.renderer.model.nodeRenderer.exceptions.OptionsAreNotSupportedByAnyRendererException;
import ai.stapi.graph.renderer.model.nodeRenderer.exceptions.OptionsAreSupportedByMultipleRenderersException;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;


public class GenericNodeRenderer {

  private final List<NodeRenderer> nodeRenderers;

  public GenericNodeRenderer(List<NodeRenderer> existingNodeRenderers) {
    this.nodeRenderers = existingNodeRenderers;
  }


  public RenderOutput render(TraversableNode node, RendererOptions options) {

    NodeRenderer renderer = this.getNodeRenderer(options);

    return renderer.render(
        node,
        options
    );
  }

  @NotNull
  private NodeRenderer getNodeRenderer(RendererOptions options) {
    var supportingNodeRenderers = nodeRenderers.stream()
        .filter(parser -> parser.supports(options))
        .collect(Collectors.toSet());

    if (supportingNodeRenderers.isEmpty()) {
      throw new OptionsAreNotSupportedByAnyRendererException(options);
    }

    if (supportingNodeRenderers.size() > 1) {
      throw new OptionsAreSupportedByMultipleRenderersException(options);
    }

    return supportingNodeRenderers.stream()
        .findFirst()
        .orElseThrow();
  }
}
