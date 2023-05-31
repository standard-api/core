package ai.stapi.graph.attribute.attributeValue;

public class UuidAttributeValue extends UriAttributeValue {

  public static final String SERIALIZATION_TYPE = "uuid";

  public UuidAttributeValue(String value) {
    super(value.strip());
  }

  @Override
  public String getDataType() {
    return SERIALIZATION_TYPE;
  }
}
