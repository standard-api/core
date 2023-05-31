package ai.stapi.graph.attribute;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import java.util.ArrayList;
import java.util.List;

public class MetaData {

  private final String name;
  private final List<AttributeValue<?>> values;

  public MetaData(
      String name,
      List<AttributeValue<?>> values
  ) {
    this.name = name;
    this.values = new ArrayList<>(values);
  }

  public MetaData(
      String name,
      AttributeValue<?> value
  ) {
    this.name = name;
    this.values = List.of(value);
  }

  public String getName() {
    return name;
  }

  public List<AttributeValue<?>> getValues() {
    return values;
  }
}
