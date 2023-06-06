package ai.stapi.graphoperations.fixtures;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GraphLoaderExpectedTestClass {

  private ExpectedEdgesClass edges;

  protected GraphLoaderExpectedTestClass() {
  }

  public GraphLoaderExpectedTestClass(
      String exampleStringAttribute,
      String exampleName,
      Integer exampleQuantity,
      ExpectedJoinedNodeClass node
  ) {
    this.edges = new ExpectedEdgesClass(
        exampleStringAttribute,
        exampleName,
        exampleQuantity,
        node
    );
  }

  public ExpectedEdgesClass getEdges() {
    return edges;
  }

  public static class ExpectedEdgesClass {

    private String exampleStringAttribute;
    private String exampleName;
    private Integer exampleQuantity;

    private ExpectedJoinedNodeClass node;

    protected ExpectedEdgesClass() {
    }

    public ExpectedEdgesClass(
        String exampleStringAttribute,
        String exampleName,
        Integer exampleQuantity,
        ExpectedJoinedNodeClass node
    ) {
      this.exampleStringAttribute = exampleStringAttribute;
      this.exampleName = exampleName;
      this.exampleQuantity = exampleQuantity;
      this.node = node;
    }

    @JsonProperty("exampleStringAttribute")
    public String getExampleStringAttribute() {
      return exampleStringAttribute;
    }

    @JsonProperty("exampleName")
    public String getExampleName() {
      return exampleName;
    }

    @JsonProperty("exampleQuantity")
    public Integer getExampleQuantity() {
      return exampleQuantity;
    }

    public ExpectedJoinedNodeClass getNode() {
      return node;
    }
  }

  public static class ExpectedJoinedNodeClass {

    private String exampleName;
    private String exampleStringAttribute;
    private Integer exampleQuantity;

    protected ExpectedJoinedNodeClass() {
    }

    public ExpectedJoinedNodeClass(
        String exampleName,
        String exampleStringAttribute,
        Integer exampleQuantity
    ) {
      this.exampleName = exampleName;
      this.exampleStringAttribute = exampleStringAttribute;
      this.exampleQuantity = exampleQuantity;
    }

    @JsonProperty("exampleName")
    public String getExampleName() {
      return exampleName;
    }

    @JsonProperty("exampleStringAttribute")
    public String getExampleStringAttribute() {
      return exampleStringAttribute;
    }

    @JsonProperty("exampleQuantity")
    public Integer getExampleQuantity() {
      return exampleQuantity;
    }
  }
}