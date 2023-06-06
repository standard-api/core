package ai.stapi.graphoperations.graphLoader.inmemory.filter;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGraphLoader;
import ai.stapi.graphoperations.graphLoader.search.SearchOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AbstractOneValueFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.GreaterThanOrEqualFilterOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.List;
import java.util.Set;

public class InMemoryGreaterThanOrEqualsFilterResolver extends InMemoryOneValueFilterResolver {

  public InMemoryGreaterThanOrEqualsFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      InMemoryGraphLoader inMemoryGraphLoader
  ) {
    super(structureSchemaFinder, inMemoryGraphLoader);
  }

  @Override
  public boolean resolveValue(AbstractOneValueFilterOption<?> option, AttributeValue<?> actualValue) {
    if (option.hasListValue()) {
      return false;
    }
    if (option.hasSetValue()) {
      return false;
    }
    return option.getValueAsAttributeValue().compareTo(actualValue) <= 0;
  }

  @Override
  protected boolean resolveValue(AbstractOneValueFilterOption<?> option, List<AttributeValue<?>> actualValue) {
    if (option.hasListValue()) {
      var valueAsListAttributeValue = option.getValueAsListAttributeValue();
      if (valueAsListAttributeValue.isEmpty()) {
        return true;
      }
      return valueAsListAttributeValue.equals(actualValue);
    }
    return option.hasSetValue() && (option.getValueAsSetAttributeValue().isEmpty());
  }

  @Override
  protected boolean resolveValue(AbstractOneValueFilterOption<?> option, Set<AttributeValue<?>> actualValue) {
    if (option.hasSetValue()) {
      var valueAsSetAttributeValue = option.getValueAsSetAttributeValue();
      if (valueAsSetAttributeValue.isEmpty()) {
        return true;
      }
      return valueAsSetAttributeValue.equals(actualValue);
    }
    return option.hasListValue() && (option.getValueAsListAttributeValue().isEmpty());
  }

  @Override
  public boolean supports(SearchOption<?> option) {
    return option instanceof GreaterThanOrEqualFilterOption<?>;
  }
}
