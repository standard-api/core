package ai.stapi.graph.renderer.model;

public class StringGraphRenderOutput implements GraphRenderOutput {

  private String graph;

  public StringGraphRenderOutput(String graphString) {
    this.graph = graphString;
  }

  @Override
  public String toPrintableString() {
    return this.graph;
  }
}
