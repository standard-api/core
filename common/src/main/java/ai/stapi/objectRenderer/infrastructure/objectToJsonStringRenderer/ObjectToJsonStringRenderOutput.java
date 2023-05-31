package ai.stapi.objectRenderer.infrastructure.objectToJsonStringRenderer;


import ai.stapi.objectRenderer.model.RenderOutput;

public class ObjectToJsonStringRenderOutput implements RenderOutput {

  private String output;

  public ObjectToJsonStringRenderOutput(String output) {
    this.output = output;
  }

  @Override
  public String toPrintableString() {
    return this.output;
  }
}
