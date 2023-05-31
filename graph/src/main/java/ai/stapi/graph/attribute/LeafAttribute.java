package ai.stapi.graph.attribute;


import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import java.sql.Timestamp;
import java.util.Map;

public class LeafAttribute<T, AV extends AttributeValue<T>> extends AbstractAttribute<T> {

  public static final String DATA_STRUCTURE_TYPE = "LeafAttribute";
  private final AV boxedValue;

  public LeafAttribute(String name, Map<String, MetaData> metaData, AV boxedValue) {
    super(name, metaData);
    this.boxedValue = boxedValue;
  }

  public LeafAttribute(String name, Timestamp createdAt, AV boxedValue) {
    super(name, createdAt);
    this.boxedValue = boxedValue;
  }

  public LeafAttribute(String name, AV boxedValue) {
    super(name);
    this.boxedValue = boxedValue;
  }

  public AV getBoxedValue() {
    return boxedValue;
  }

  @Override
  public T getValue() {
    return this.getBoxedValue().getValue();
  }

  @Override
  public LeafAttribute<T, AV> getCopy() {
    return new LeafAttribute<>(this.getName(), this.getCreatedAt(), this.getBoxedValue());
  }
}
