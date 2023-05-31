package ai.stapi.graph;

public class NodeTypeInfo {

  private String type;
  private Long count;

  public NodeTypeInfo(String type, Long count) {
    this.type = type;
    this.count = count;
  }

  public String getType() {
    return type;
  }

  public Long getCount() {
    return count;
  }
}
