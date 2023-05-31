package ai.stapi.graph.attribute.attributeValue;

public class PositiveIntegerAttributeValue extends IntegerAttributeValue {

  public static final String SERIALIZATION_TYPE = "positiveInt";

  public PositiveIntegerAttributeValue(Integer value) {
    super(value);
  }

  @Override
  public String getDataType() {
    return SERIALIZATION_TYPE;
  }
}
