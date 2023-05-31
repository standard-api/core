package ai.stapi.graph.renderer.model.edgeRenderer;

import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import ai.stapi.graph.renderer.model.RenderOutput;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;

public interface EdgeRenderer {

  RenderOutput render(TraversableEdge edge);

  RenderOutput render(TraversableEdge edge, RendererOptions options);
}
