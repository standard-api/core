package ai.stapi.graph.attribute.attributeValue;

public class StringAttributeValue extends AbstractAttributeValue<String> implements StringLikeAttributeValue<String> {

  public static final String SERIALIZATION_TYPE = "string";

  public StringAttributeValue(String value) {
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
