package ai.stapi.graphoperations.graphLoader.search.filterOption;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graph.attribute.attributeValue.DecimalAttributeValue;
import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLoader.search.exceptions.UnsupportedFilterOptionAttributeValueType;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractOneValueFilterOption<ValueType> 
    extends AbstractLeafFilterOption<OneValueFilterOptionParameters<ValueType>> {

  protected AbstractOneValueFilterOption() {
  }

  protected AbstractOneValueFilterOption(
      String operation, 
      String attributeName,
      ValueType attributeValue
  ) {
    super(operation, new OneValueFilterOptionParameters<>(attributeName, attributeValue));
    this.ensureValidAttributeValueType(attributeValue);
  }

  protected AbstractOneValueFilterOption(
      String strategy,
      PositiveGraphDescription attributePathName,
      ValueType attributeValue
  ) {
    super(strategy, new OneValueFilterOptionParameters<>(attributePathName, attributeValue));
    this.ensureValidAttributeValueType(attributeValue);
  }

  protected abstract List<Class<?>> setAllowedValueTypes();

  public boolean hasListValue() {
    return this.getParameters().getAttributeValue() instanceof List<?>;
  }
  
  public boolean hasSetValue() {
    return this.getParameters().getAttributeValue() instanceof Set<?>;
  }
  
  public List<AttributeValue<?>> getValueAsListAttributeValue() {
    if (!this.hasListValue()) {
      throw new RuntimeException("Cannot provide value as list attribute value.");
    }
    var listValue = (List<?>) this.getParameters().getAttributeValue();
    return listValue.stream().map(this::convertToAttributeValue).collect(Collectors.toList());
  }

  public Set<AttributeValue<?>> getValueAsSetAttributeValue() {
    if (!this.hasSetValue()) {
      throw new RuntimeException("Cannot provide value as set attribute value.");
    }
    var setValue = (Set<?>) this.getParameters().getAttributeValue();
    return setValue.stream().map(this::convertToAttributeValue).collect(Collectors.toSet());
  }

  public AttributeValue<?> getValueAsAttributeValue() {
    if (this.hasSetValue() || this.hasListValue()) {
      throw new RuntimeException("Cannot provide value as single attribute value.");
    }
    return this.convertToAttributeValue(this.getParameters().getAttributeValue());
  }

  private void ensureValidAttributeValueType(ValueType value) {
    if (value == null) {
      throw new UnsupportedFilterOptionAttributeValueType(this.getClass());
    }
    if (!this.setAllowedValueTypes().contains(value.getClass())) {
      throw new UnsupportedFilterOptionAttributeValueType(value.getClass(), this.getClass());
    }
  }

  private AttributeValue<?> convertToAttributeValue(Object value) {
    if (value instanceof String string) {
      return new StringAttributeValue(string);
    }
    if (value instanceof Double doubleValue) {
      return new DecimalAttributeValue(doubleValue);
    }
    if (value instanceof Float floatValue) {
      return new DecimalAttributeValue(floatValue.doubleValue());
    }
    if (value instanceof Integer integerValue) {
      return new IntegerAttributeValue(integerValue);
    }
    if (value instanceof Boolean booleanValue) {
      return new BooleanAttributeValue(booleanValue);
    }
    throw new RuntimeException(
        String.format(
            "Cannot convert to attribute value, because unknown value type encountered.%nActual type: %s",
            value.getClass().getSimpleName()
        )
    );
  }
}
