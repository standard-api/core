package ai.stapi.graph.attribute.attributeValue;

public class Base64BinaryAttributeValue extends AbstractAttributeValue<String> implements StringLikeAttributeValue<String> {

  public static final String SERIALIZATION_TYPE = "base64Binary";

  public Base64BinaryAttributeValue(String value) {
    super(value.strip());
  }

  @Override
  public String getDataType() {
    return SERIALIZATION_TYPE;
  }

  @Override
  public String toStringValue() {
    return this.getValue();
  }
}
