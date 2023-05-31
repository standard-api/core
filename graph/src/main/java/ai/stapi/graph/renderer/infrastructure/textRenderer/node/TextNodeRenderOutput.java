package ai.stapi.graph.renderer.infrastructure.textRenderer.node;

import ai.stapi.graph.renderer.model.RenderOutput;

public class TextNodeRenderOutput implements RenderOutput {

  private String renderedString;

  public TextNodeRenderOutput(String renderedString) {
    this.renderedString = renderedString;
  }

  public String toPrintableString() {
    return renderedString;
  }

  public void setRenderedString(String renderedString) {
    this.renderedString = renderedString;
  }
}
