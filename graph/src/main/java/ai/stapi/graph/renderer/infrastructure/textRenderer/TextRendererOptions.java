package ai.stapi.graph.renderer.infrastructure.textRenderer;

import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;

public class TextRendererOptions implements RendererOptions {

  private int indentLevel;

  public TextRendererOptions() {
    this.indentLevel = 0;
  }

  public TextRendererOptions(int indentLevel) {
    this.indentLevel = indentLevel;
  }

  public int getIndentLevel() {
    return indentLevel;
  }

}
