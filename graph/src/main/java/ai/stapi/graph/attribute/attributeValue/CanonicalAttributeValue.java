package ai.stapi.graph.attribute.attributeValue;

public class CanonicalAttributeValue extends UriAttributeValue {

  public static final String SERIALIZATION_TYPE = "canonical";

  public CanonicalAttributeValue(String value) {
    super(value.strip());
  }

  @Override
  public String getDataType() {
    return SERIALIZATION_TYPE;
  }
}
