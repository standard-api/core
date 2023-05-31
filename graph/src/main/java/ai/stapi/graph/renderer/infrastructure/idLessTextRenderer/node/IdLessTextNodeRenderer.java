package ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.node;

import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.IdLessTextRendererOptions;
import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.attribute.TextAttributeContainerRenderer;
import ai.stapi.graph.renderer.infrastructure.textRenderer.TextRendererOptions;
import ai.stapi.graph.renderer.model.nodeRenderer.NodeRenderer;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;
import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.utils.LineFormatter;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;


public class IdLessTextNodeRenderer implements NodeRenderer {

  private final TextAttributeContainerRenderer textAttributeContainerRenderer;

  @Autowired
  public IdLessTextNodeRenderer(TextAttributeContainerRenderer textAttributeContainerRenderer) {
    this.textAttributeContainerRenderer = textAttributeContainerRenderer;
  }

  @Override
  public boolean supports(RendererOptions options) {
    return options.getClass().equals(IdLessTextRendererOptions.class);
  }

  @Override
  public IdLessTextNodeRenderOutput render(TraversableNode node) {
    return this.render(
        node,
        new IdLessTextRendererOptions()
    );
  }

  @Override
  public IdLessTextNodeRenderOutput render(TraversableNode node, RendererOptions options) {
    var downtypedOptions = (IdLessTextRendererOptions) options;
    var indentLevel = downtypedOptions.getIndentLevel();
    var relationAnnotation = downtypedOptions.getNodeRelationAnnotationAttributeName();

    var nodeTypeLine = LineFormatter.createLine(
        "node_type: " + node.getType(),
        indentLevel
    );
    var hashLine = LineFormatter.createLine(
        "node_hash: " + this.formatHashCodeAsString(node.getIdlessHashCodeWithEdges()),
        indentLevel
    );
    var edgesString = this.getRenderedEdges(
        node,
        indentLevel,
        relationAnnotation
    );
    var attributesString = this.textAttributeContainerRenderer.render(
        node,
        new TextRendererOptions(indentLevel)
    ).toPrintableString();
    return new IdLessTextNodeRenderOutput(
        nodeTypeLine + hashLine + edgesString + attributesString
    );
  }

  private String formatHashCodeAsString(int hashCode) {
    var bytes = DigestUtils.md5(hashCode + "");
    var hex = Hex.encodeHex(bytes);
    return new String(hex).toUpperCase().substring(0, 5);
  }

  private String getRenderedEdges(TraversableNode node, int indentLevel,
      Optional<String> annotationAttribute) {
    var line = LineFormatter.createLine(
        "node_edges:",
        indentLevel
    );
    var edges = node.getEdges();
    return line + edges.stream()
        .map(edge -> this.getRenderedEdge(
            edge,
            indentLevel + 1,
            annotationAttribute
        ))
        .sorted()
        .collect(Collectors.joining());
  }

  private String getRenderedEdge(TraversableEdge edge, int indentLevel,
      Optional<String> annotationAttribute) {
    var nodeFrom = edge.getNodeFrom();
    var nodeTo = edge.getNodeTo();
    var typeRelationString = String.format(
        "%s -> %s -> %s",
        nodeFrom.getType(),
        edge.getType(),
        nodeTo.getType()
    );

    var relationString = "";
    if (annotationAttribute.isPresent()) {
      relationString = this.getAttributeRelationString(edge, annotationAttribute.get());

    } else {
      relationString = this.getHashRelationString(edge);
    }

    return LineFormatter.createLine(
        typeRelationString + " " + relationString,
        indentLevel
    );
  }

  private String getHashRelationString(TraversableEdge edge) {
    return String.format(
        "(%s -> %s -> %s)",
        this.formatHashCodeAsString(edge.getNodeFrom().getIdlessHashCodeWithEdges()),
        this.formatHashCodeAsString(edge.getIdlessHashCode()),
        this.formatHashCodeAsString(edge.getNodeTo().getIdlessHashCodeWithEdges())
    );
  }

  private String getAttributeRelationString(TraversableEdge edge, String attributeName) {
    var nodeFromTag = "";
    var nodeToTag = "";
    if (edge.getNodeFrom().hasAttribute(attributeName)) {
      nodeFromTag = (String) edge.getNodeFrom().getAttribute(attributeName).getValue();
    } else {
      nodeFromTag = this.formatHashCodeAsString(edge.getNodeFrom().getIdlessHashCodeWithEdges());
    }
    if (edge.getNodeTo().hasAttribute(attributeName)) {
      nodeToTag = (String) edge.getNodeTo().getAttribute(attributeName).getValue();
    } else {
      nodeToTag = this.formatHashCodeAsString(edge.getNodeTo().getIdlessHashCodeWithEdges());
    }

    return String.format(
        "(%s -> %s -> %s)",
        nodeFromTag,
        this.formatHashCodeAsString(edge.getIdlessHashCode()),
        nodeToTag
    );
  }
}
