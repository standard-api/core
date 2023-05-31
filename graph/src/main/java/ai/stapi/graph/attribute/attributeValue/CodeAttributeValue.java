package ai.stapi.graph.attribute.attributeValue;

public class CodeAttributeValue extends StringAttributeValue {

  public static final String SERIALIZATION_TYPE = "code";

  public CodeAttributeValue(String value) {
    super(value);
  }

  @Override
  public String getDataType() {
    return CodeAttributeValue.SERIALIZATION_TYPE;
  }
}
