package ai.stapi.graph.renderer.infrastructure.apiRenderer.reponseGraph;

import ai.stapi.graph.attribute.Attribute;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import java.time.Instant;

public class AttributeVersionResponse implements Serializable {

  private final String value;

  private final Instant createdAt;

  public AttributeVersionResponse(Attribute<?> attribute) {
    this.value = attribute.getValue().toString();
    this.createdAt = attribute.getCreatedAt();
  }

  @JsonCreator
  public AttributeVersionResponse(String value, Instant createdAt) {
    this.value = value;
    this.createdAt = createdAt;
  }

  public String getValue() {
    return value;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
