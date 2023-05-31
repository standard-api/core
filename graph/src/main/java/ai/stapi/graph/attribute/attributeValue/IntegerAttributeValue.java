package ai.stapi.graph.attribute.attributeValue;

public class IntegerAttributeValue extends AbstractAttributeValue<Integer> implements NumberLikeAttributeValue<Integer> {

  public static final String SERIALIZATION_TYPE = "integer";

  public IntegerAttributeValue(Integer value) {
    super(value);
  }

  @Override
  public String getDataType() {
    return SERIALIZATION_TYPE;
  }

  @Override
  public Double toDoubleValue() {
    return this.getValue().doubleValue();
  }
}
