package ai.stapi.graphoperations.graphReader.readResults;

import ai.stapi.graph.attribute.Attribute;

public class AttributeReadResult implements ValueReadResult {

  private final Attribute<?> attribute;

  public AttributeReadResult(Attribute<?> value) {
    this.attribute = value;
  }

  public Attribute<?> getAttribute() {
    return attribute;
  }

  @Override
  public Object getValue() {
    return this.attribute.getValue();
  }
}
