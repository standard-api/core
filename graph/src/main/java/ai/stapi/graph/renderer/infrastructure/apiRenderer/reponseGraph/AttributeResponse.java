package ai.stapi.graph.renderer.infrastructure.apiRenderer.reponseGraph;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AttributeResponse implements Serializable {

  private final String type;
  private final List<AttributeVersionResponse> values;

  @JsonCreator
  public AttributeResponse(@NotNull String type, @NotNull List<AttributeVersionResponse> values) {
    this.type = type;
    this.values = values;
  }

  public String getType() {
    return type;
  }

  public List<AttributeVersionResponse> getValues() {
    return values;
  }

  @JsonIgnore
  public String getCurrentValue() {
    return values.stream()
        .findFirst()
        .orElseThrow()
        .getValue();
  }
}
