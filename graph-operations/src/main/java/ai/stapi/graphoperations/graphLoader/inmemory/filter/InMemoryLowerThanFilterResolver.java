package ai.stapi.graphoperations.graphLoader.inmemory.filter;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGraphLoader;
import ai.stapi.graphoperations.graphLoader.search.SearchOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AbstractOneValueFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.LowerThanFilterOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.List;
import java.util.Set;

public class InMemoryLowerThanFilterResolver extends InMemoryOneValueFilterResolver {

  public InMemoryLowerThanFilterResolver(
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
    return option.getValueAsAttributeValue().compareTo(actualValue) > 0;
  }

  @Override
  protected boolean resolveValue(AbstractOneValueFilterOption<?> option, List<AttributeValue<?>> actualValue) {
    if (option.hasListValue() && !(option.getValueAsListAttributeValue().isEmpty())) {
      return actualValue.isEmpty();
    }
    if (option.hasSetValue() && !(option.getValueAsSetAttributeValue().isEmpty())) {
      return actualValue.isEmpty();
    }
    return false;
  }

  @Override
  protected boolean resolveValue(AbstractOneValueFilterOption<?> option, Set<AttributeValue<?>> actualValue) {
    if (option.hasListValue() && !(option.getValueAsListAttributeValue().isEmpty())) {
      return actualValue.isEmpty();
    }
    if (option.hasSetValue() && !(option.getValueAsSetAttributeValue().isEmpty())) {
      return actualValue.isEmpty();
    }
    return false;
  }

  @Override
  public boolean supports(SearchOption<?> option) {
    return option instanceof LowerThanFilterOption<?>;
  }
}
