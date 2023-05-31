package ai.stapi.graph.attribute.attributeValue;

public class UnknownAttributeValue extends AbstractAttributeValue<Object> implements StringLikeAttributeValue<Object> {

  public static final String SERIALIZATION_TYPE = "unknown";

  public UnknownAttributeValue(Object value) {
    super(value);
  }

  @Override
  public String getDataType() {
    return UnknownAttributeValue.SERIALIZATION_TYPE;
  }

  @Override
  public String toStringValue() {
    return this.getValue().toString();
  }
}
