package ai.stapi.graph.attribute.attributeValue;

public class UrlAttributeValue extends UriAttributeValue {

  public static final String SERIALIZATION_TYPE = "url";

  public UrlAttributeValue(String value) {
    super(value.strip());
  }

  @Override
  public String getDataType() {
    return SERIALIZATION_TYPE;
  }
}
