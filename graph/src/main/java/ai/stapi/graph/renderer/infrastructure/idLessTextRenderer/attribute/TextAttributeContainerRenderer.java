package ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.attribute;

import ai.stapi.graph.attribute.AbstractAttributeContainer;
import ai.stapi.graph.renderer.infrastructure.textRenderer.TextRendererOptions;
import ai.stapi.graph.renderer.infrastructure.textRenderer.node.TextNodeRenderOutput;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;
import ai.stapi.utils.LineFormatter;
import java.util.stream.Collectors;


public class TextAttributeContainerRenderer {

  public TextNodeRenderOutput render(AbstractAttributeContainer attributeContainer,
                                     RendererOptions options) {
    var downtypedOptions = (TextRendererOptions) options;
    var indentLevel = downtypedOptions.getIndentLevel();

    return new TextNodeRenderOutput(
        LineFormatter.createLine(
            "attributes:",
            indentLevel
        ) + attributeContainer.getFlattenAttributes().stream()
            .map(attribute -> attribute.getName() + " -> " + attribute.getValue())
            .map(line -> LineFormatter.createLine(
                line,
                indentLevel + 1
            ))
            .sorted()
            .collect(Collectors.joining())
    );
  }
}
