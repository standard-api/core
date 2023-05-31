package ai.stapi.graph.renderer.model.nodeRenderer;

import ai.stapi.graph.renderer.model.RenderOutput;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import org.jetbrains.annotations.NotNull;



public interface NodeRenderer {

  RenderOutput render(TraversableNode node);

  RenderOutput render(TraversableNode node, @NotNull RendererOptions options);

  boolean supports(@NotNull RendererOptions options);
}
