package ai.stapi.graph.attribute.attributeValue;

public class DecimalAttributeValue extends AbstractAttributeValue<Double> implements NumberLikeAttributeValue<Double> {

  public static final String SERIALIZATION_TYPE = "decimal";

  public DecimalAttributeValue(Double value) {
    super(value);
  }

  @Override
  public String getDataType() {
    return SERIALIZATION_TYPE;
  }

  @Override
  public Double toDoubleValue() {
    return this.getValue();
  }
}
