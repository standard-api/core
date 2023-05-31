package ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.node;

import ai.stapi.graph.renderer.model.RenderOutput;

public class IdLessTextNodeRenderOutput implements RenderOutput {

  private String renderedString;

  public IdLessTextNodeRenderOutput(String renderedString) {
    this.renderedString = renderedString;
  }

  @Override
  public String toPrintableString() {
    return renderedString;
  }

  public void setRenderedString(String renderedString) {
    this.renderedString = renderedString;
  }
}
