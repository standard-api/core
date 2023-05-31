package ai.stapi.graph.attribute.attributeValue;

public class XhtmlAttributeValue extends AbstractAttributeValue<String> implements StringLikeAttributeValue<String> {

  public static final String SERIALIZATION_TYPE = "xhtml";

  public XhtmlAttributeValue(String value) {
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
