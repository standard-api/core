package ai.stapi.graph.renderer.infrastructure.textRenderer.edge;

import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.edge.IdLessTextEdgeRenderer;
import ai.stapi.graph.renderer.model.edgeRenderer.EdgeRenderer;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;
import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import org.springframework.beans.factory.annotation.Autowired;


public class TextEdgeRenderer implements EdgeRenderer {

  private IdLessTextEdgeRenderer idLessTextEdgeRenderer;

  @Autowired
  public TextEdgeRenderer(IdLessTextEdgeRenderer idLessTextEdgeRenderer) {
    this.idLessTextEdgeRenderer = idLessTextEdgeRenderer;
  }

  @Override
  public TextEdgeRenderOutput render(TraversableEdge edge) {
    var edgeStringWithoutId = idLessTextEdgeRenderer.render(edge).toPrintableString();
    var edgeId = edge.getId();
    return new TextEdgeRenderOutput(
        String.format(
            "edge_id: %s\n",
            edgeId
        ) + edgeStringWithoutId
    );
  }

  @Override
  public TextEdgeRenderOutput render(TraversableEdge edge, RendererOptions options) {
    return render(edge);
  }
}
