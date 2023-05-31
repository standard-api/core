package ai.stapi.graph.renderer.infrastructure.textRenderer.node;

import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.IdLessTextRendererOptions;
import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.node.IdLessTextNodeRenderer;
import ai.stapi.graph.renderer.infrastructure.textRenderer.TextRendererOptions;
import ai.stapi.graph.renderer.model.RenderOutput;
import ai.stapi.graph.renderer.model.nodeRenderer.NodeRenderer;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.utils.LineFormatter;
import org.springframework.beans.factory.annotation.Autowired;


public class TextNodeRenderer implements NodeRenderer {

  private IdLessTextNodeRenderer idLessTextNodeRenderer;

  @Autowired
  public TextNodeRenderer(IdLessTextNodeRenderer idLessTextNodeRenderer) {
    this.idLessTextNodeRenderer = idLessTextNodeRenderer;
  }

  @Override
  public boolean supports(RendererOptions options) {
    return options.getClass().equals(TextRendererOptions.class);
  }

  @Override
  public RenderOutput render(TraversableNode node) {
    return this.render(
        node,
        new TextRendererOptions()
    );
  }

  @Override
  public RenderOutput render(TraversableNode node, RendererOptions options) {
    var downtypedOptions = (TextRendererOptions) options;
    var indentLevel = downtypedOptions.getIndentLevel();

    var idLessRenderedNodeString = idLessTextNodeRenderer.render(
        node,
        new IdLessTextRendererOptions(indentLevel)
    ).toPrintableString();

    var renderedNodeIdLine = LineFormatter.createLine(
        "node_id: " + node.getId(),
        indentLevel
    );

    return new TextNodeRenderOutput(
        renderedNodeIdLine +
            idLessRenderedNodeString
    );
  }
}
