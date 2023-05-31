package ai.stapi.graph.attribute;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SetAttribute extends AbstractAttribute<Set<?>> {

  public static final String DATA_STRUCTURE_TYPE = "SetAttribute";

  private final Set<AttributeValue<?>> boxedValues;

  public SetAttribute(String name, Timestamp createdAt, Set<AttributeValue<?>> values) {
    super(name, createdAt);
    this.boxedValues = values;
  }

  public SetAttribute(String name, Timestamp createdAt) {
    this(name, createdAt, Set.of());
  }

  public SetAttribute(String name, Map<String, MetaData> metaData, Set<AttributeValue<?>> values) {
    super(name, metaData);
    this.boxedValues = values;
  }

  public SetAttribute(String name, Map<String, MetaData> metaData) {
    this(name, metaData, Set.of());
  }

  public SetAttribute(String name, Set<AttributeValue<?>> values) {
    this(name, new HashMap<>(), values);
  }

  public SetAttribute(String name, AttributeValue<?>... values) {
    this(name, Arrays.stream(values).collect(Collectors.toSet()));
  }

  public SetAttribute(String name, Timestamp createdAt, AttributeValue<?>... values) {
    this(name, createdAt, Arrays.stream(values).collect(Collectors.toSet()));
  }

  public SetAttribute(String name, Map<String, MetaData> metaData, AttributeValue<?>... values) {
    this(name, metaData, Arrays.stream(values).collect(Collectors.toSet()));
  }

  public Set<AttributeValue<?>> getBoxedValues() {
    return boxedValues;
  }

  @Override
  public Set<?> getValue() {
    return this.boxedValues.stream().map(AttributeValue::getValue).collect(Collectors.toSet());
  }

  @Override
  public SetAttribute getCopy() {
    return new SetAttribute(this.getName(), this.getMetaData(), this.getBoxedValues());
  }
}
