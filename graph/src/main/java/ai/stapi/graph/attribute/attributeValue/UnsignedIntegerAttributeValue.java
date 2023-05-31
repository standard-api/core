package ai.stapi.graph.attribute.attributeValue;

public class UnsignedIntegerAttributeValue extends IntegerAttributeValue {

  public static final String SERIALIZATION_TYPE = "unsignedInt";

  public UnsignedIntegerAttributeValue(Integer value) {
    super(value);
  }

  @Override
  public String getDataType() {
    return SERIALIZATION_TYPE;
  }
}
