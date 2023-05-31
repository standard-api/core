package ai.stapi.graph;

public class EdgeTypeInfo {

  private String type;
  private Long count;

  public EdgeTypeInfo(String type, Long count) {
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
