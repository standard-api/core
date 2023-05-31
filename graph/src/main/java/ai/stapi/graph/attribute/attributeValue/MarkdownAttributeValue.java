package ai.stapi.graph.attribute.attributeValue;

public class MarkdownAttributeValue extends StringAttributeValue {

  public static final String SERIALIZATION_TYPE = "markdown";

  public MarkdownAttributeValue(String value) {
    super(value);
  }

  @Override
  public String getDataType() {
    return MarkdownAttributeValue.SERIALIZATION_TYPE;
  }
}
