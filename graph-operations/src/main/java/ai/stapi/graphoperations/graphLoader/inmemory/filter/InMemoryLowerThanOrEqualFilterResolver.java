package ai.stapi.graphoperations.graphLoader.inmemory.filter;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGraphLoader;
import ai.stapi.graphoperations.graphLoader.search.SearchOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AbstractOneValueFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.LowerThanOrEqualsFilterOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.List;
import java.util.Set;

public class InMemoryLowerThanOrEqualFilterResolver extends InMemoryOneValueFilterResolver {

  public InMemoryLowerThanOrEqualFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      InMemoryGraphLoader inMemoryGraphLoader
  ) {
    super(structureSchemaFinder, inMemoryGraphLoader);
  }

  @Override
  public boolean resolveValue(AbstractOneValueFilterOption<?> option, AttributeValue<?> actualValue) {
    if (option.hasListValue()) {
      return true;
    }
    if (option.hasSetValue()) {
      return true;
    }
    return option.getValueAsAttributeValue().compareTo(actualValue) >= 0;
  }

  @Override
  protected boolean resolveValue(AbstractOneValueFilterOption<?> option, List<AttributeValue<?>> actualValue) {
    if (option.hasListValue()) {
      if (actualValue.isEmpty()) {
        return true;
      }
      return option.getValueAsListAttributeValue().equals(actualValue);
    }
    return option.hasSetValue() && actualValue.isEmpty();
  }

  @Override
  protected boolean resolveValue(AbstractOneValueFilterOption<?> option, Set<AttributeValue<?>> actualValue) {
    if (option.hasSetValue()) {
      if (actualValue.isEmpty()) {
        return true;
      }
      return option.getValueAsSetAttributeValue().equals(actualValue);
    }
    return option.hasListValue() && actualValue.isEmpty();
  }

  @Override
  public boolean supports(SearchOption<?> option) {
    return option instanceof LowerThanOrEqualsFilterOption<?>;
  }
}
