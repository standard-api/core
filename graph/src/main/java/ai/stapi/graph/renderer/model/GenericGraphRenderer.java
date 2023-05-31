package ai.stapi.graph.renderer.model;

import ai.stapi.graph.Graph;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;
import ai.stapi.graph.renderer.model.nodeRenderer.exceptions.OptionsAreNotSupportedByAnyRendererException;
import ai.stapi.graph.renderer.model.nodeRenderer.exceptions.OptionsAreSupportedByMultipleRenderersException;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;


public class GenericGraphRenderer {

  private final List<GraphRenderer> graphRenderers;

  @Autowired
  public GenericGraphRenderer(
      List<GraphRenderer> existingGraphRenderers) {
    this.graphRenderers = existingGraphRenderers;
  }

  public RenderOutput render(Graph graph, RendererOptions options) {

    return this.render(
        new InMemoryGraphRepository(graph),
        options
    );
  }

  public RenderOutput render(InMemoryGraphRepository graph,
      RendererOptions options) {

    GraphRenderer renderer = this.getGraphRenderer(options);

    return renderer.render(
        graph,
        options
    );
  }

  @NotNull
  private GraphRenderer getGraphRenderer(RendererOptions options) {
    var supportingGraphRenderers = graphRenderers.stream()
        .filter(parser -> parser.supports(options))
        .collect(Collectors.toSet());

    if (supportingGraphRenderers.isEmpty()) {
      throw new OptionsAreNotSupportedByAnyRendererException(options);
    }

    if (supportingGraphRenderers.size() > 1) {
      throw new OptionsAreSupportedByMultipleRenderersException(options);
    }

    return supportingGraphRenderers.stream()
        .findFirst()
        .orElseThrow();
  }
}
