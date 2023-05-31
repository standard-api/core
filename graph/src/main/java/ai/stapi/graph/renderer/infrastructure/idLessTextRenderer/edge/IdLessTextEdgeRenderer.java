package ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.edge;

import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.IdLessTextRendererOptions;
import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.attribute.TextAttributeContainerRenderer;
import ai.stapi.graph.renderer.infrastructure.textRenderer.TextRendererOptions;
import ai.stapi.graph.renderer.model.edgeRenderer.EdgeRenderer;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;
import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import ai.stapi.utils.LineFormatter;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;


public class IdLessTextEdgeRenderer implements EdgeRenderer {

  private final TextAttributeContainerRenderer textAttributeContainerRenderer;

  @Autowired
  public IdLessTextEdgeRenderer(TextAttributeContainerRenderer textAttributeContainerRenderer) {
    this.textAttributeContainerRenderer = textAttributeContainerRenderer;
  }

  @Override
  public IdlessTextRenderedOutput render(TraversableEdge edge) {
    return this.render(
        edge,
        new IdLessTextRendererOptions()
    );
  }

  @Override
  public IdlessTextRenderedOutput render(TraversableEdge edge, RendererOptions options) {
    var specificOptions = (IdLessTextRendererOptions) options;

    var typeLine = LineFormatter.createLine(
        "edge_type: " + edge.getType(),
        specificOptions.getIndentLevel()
    );
    var hashLine = LineFormatter.createLine(
        "edge_hash: " + this.formatHashCodeAsString(edge.getIdlessHashCode()),
        specificOptions.getIndentLevel()

    );
    var relationLine = this.getRelationLine(
        edge,
        specificOptions
    );
    var attributesLines = textAttributeContainerRenderer.render(
        edge,
        new TextRendererOptions(specificOptions.getIndentLevel())
    ).toPrintableString();

    return new IdlessTextRenderedOutput(typeLine + hashLine + relationLine + attributesLines);
  }

  private String getRelationLine(TraversableEdge edge, IdLessTextRendererOptions options) {
    var nodeFrom = edge.getNodeFrom();
    var nodeTo = edge.getNodeTo();
    var typeRelationString = String.format(
        "%s -> %s -> %s",
        nodeFrom.getType(),
        edge.getType(),
        nodeTo.getType()
    );

    var relationString = "";
    var attributeName = options.getNodeRelationAnnotationAttributeName();
    if (attributeName.isPresent()) {
      relationString = this.getAttributeRelationString(
          edge,
          attributeName.get()
      );

    } else {
      relationString = this.getHashRelationString(edge);
    }
    return LineFormatter.createLine(typeRelationString + " " + relationString,
        options.getIndentLevel());
  }

  private String formatHashCodeAsString(int hashCode) {
    var bytes = DigestUtils.md5(hashCode + "");
    var hex = Hex.encodeHex(bytes);
    return new String(hex).toUpperCase().substring(0, 5);
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
