package ai.stapi.graph.renderer.infrastructure.textRenderer.edge;

import ai.stapi.graph.renderer.model.RenderOutput;

public class TextEdgeRenderOutput implements RenderOutput {

  private String output;

  public TextEdgeRenderOutput(String output) {
    this.output = output;
  }

  @Override
  public String toPrintableString() {
    return this.output;
  }
}
