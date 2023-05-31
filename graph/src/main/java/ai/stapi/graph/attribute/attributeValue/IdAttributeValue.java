package ai.stapi.graph.attribute.attributeValue;

public class IdAttributeValue extends StringAttributeValue {

  public static final String SERIALIZATION_TYPE = "id";

  public IdAttributeValue(String value) {
    super(value);
  }

  @Override
  public String getDataType() {
    return IdAttributeValue.SERIALIZATION_TYPE;
  }
}
