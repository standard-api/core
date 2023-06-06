package ai.stapi.graphoperations.serializableGraph;

import ai.stapi.graph.attribute.attributeFactory.AttributeValueFactoryInput;
import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SerializableAttributeValue {

  private final String serializationType;
  private final Object value;

  @JsonCreator
  public SerializableAttributeValue(
      @JsonProperty("serializationType") String serializationType,
      @JsonProperty("value") Object value
  ) {
    this.serializationType = serializationType;
    this.value = value;
  }
  
  public SerializableAttributeValue(AttributeValue<?> attributeValue) {
    this.serializationType = attributeValue.getDataType();
    this.value = attributeValue.getValue();
  }
  
  public AttributeValueFactoryInput toAttributeFactoryInput() {
    return new AttributeValueFactoryInput(
        this.value,
        this.serializationType
    );
  }

  public String getSerializationType() {
    return serializationType;
  }

  public Object getValue() {
    return value;
  }
}
