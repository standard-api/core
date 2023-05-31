package ai.stapi.graph.attribute.attributeValue;

public class OidAttributeValue extends UriAttributeValue {

  public static final String SERIALIZATION_TYPE = "oid";

  public OidAttributeValue(String value) {
    super(value.strip());
  }

  @Override
  public String getDataType() {
    return SERIALIZATION_TYPE;
  }
}
