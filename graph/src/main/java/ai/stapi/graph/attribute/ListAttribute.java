package ai.stapi.graph.attribute;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListAttribute extends AbstractAttribute<List<?>> {

  public static final String DATA_STRUCTURE_TYPE = "ListAttribute";

  private final List<AttributeValue<?>> boxedValues;

  public ListAttribute(String name, Timestamp createdAt, List<AttributeValue<?>> values) {
    super(name, createdAt);
    this.boxedValues = values;
  }

  public ListAttribute(String name, Timestamp createdAt) {
    super(name, createdAt);
    this.boxedValues = new ArrayList<>();
  }

  public ListAttribute(String name, Map<String, MetaData> metaData) {
    super(name, metaData);
    this.boxedValues = new ArrayList<>();
  }

  public ListAttribute(String name, Map<String, MetaData> metaData,
      List<AttributeValue<?>> values) {
    super(name, metaData);
    this.boxedValues = values;
  }

  public ListAttribute(String name, List<AttributeValue<?>> values) {
    this(name, new HashMap<>(), values);
  }

  public ListAttribute(String name, AttributeValue<?>... values) {
    this(name, Arrays.stream(values).collect(Collectors.toList()));
  }

  public ListAttribute(String name, Timestamp createdAt, AttributeValue<?>... values) {
    this(name, createdAt, Arrays.stream(values).collect(Collectors.toList()));
  }

  public ListAttribute(String name, Map<String, MetaData> metaData, AttributeValue<?>... values) {
    this(name, metaData, Arrays.stream(values).collect(Collectors.toList()));
  }

  public List<AttributeValue<?>> getBoxedValues() {
    return boxedValues;
  }

  @Override
  public List<?> getValue() {
    return this.boxedValues.stream().map(AttributeValue::getValue).toList();
  }

  @Override
  public ListAttribute getCopy() {
    return new ListAttribute(this.getName(), this.getMetaData(), this.getBoxedValues());
  }
}
