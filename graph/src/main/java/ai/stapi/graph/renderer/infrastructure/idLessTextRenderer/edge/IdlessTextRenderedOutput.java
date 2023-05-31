package ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.edge;

import ai.stapi.graph.renderer.model.RenderOutput;

public class IdlessTextRenderedOutput implements RenderOutput {

  private String render;

  public IdlessTextRenderedOutput(String render) {
    this.render = render;
  }

  @Override
  public String toPrintableString() {
    return this.render;
  }
}
