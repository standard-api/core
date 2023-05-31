package ai.stapi.graph.renderer.model;

import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;

public interface GraphRenderer {

  GraphRenderOutput render(InMemoryGraphRepository graph);

  GraphRenderOutput render(InMemoryGraphRepository graph, RendererOptions options);

  boolean supports(RendererOptions options);
}
