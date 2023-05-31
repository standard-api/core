package ai.stapi.graph.renderer.infrastructure.idLessTextRenderer;

import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;
import java.util.Optional;

public class IdLessTextRendererOptions implements RendererOptions {

  private int indentLevel;

  private String nodeRelationAnnotationAttributeName;

  public IdLessTextRendererOptions() {
    this.indentLevel = 0;
    this.nodeRelationAnnotationAttributeName = "";
  }

  public IdLessTextRendererOptions(int indentLevel) {
    this.indentLevel = indentLevel;
    this.nodeRelationAnnotationAttributeName = "";
  }

  public IdLessTextRendererOptions(int indentLevel, String attributeToRenderInRelationsName) {
    this.indentLevel = indentLevel;
    this.nodeRelationAnnotationAttributeName = attributeToRenderInRelationsName;
  }

  public IdLessTextRendererOptions(String attributeToRenderInRelationsName) {
    this.indentLevel = 0;
    this.nodeRelationAnnotationAttributeName = attributeToRenderInRelationsName;
  }

  public int getIndentLevel() {
    return indentLevel;
  }

  public Optional<String> getNodeRelationAnnotationAttributeName() {
    if (this.nodeRelationAnnotationAttributeName.isBlank()) {
      return Optional.empty();
    }
    return Optional.of(nodeRelationAnnotationAttributeName);
  }
}
