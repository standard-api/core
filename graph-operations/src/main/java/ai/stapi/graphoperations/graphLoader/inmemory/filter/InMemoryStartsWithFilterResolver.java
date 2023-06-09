package ai.stapi.graphoperations.graphLoader.inmemory.filter;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringLikeAttributeValue;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGraphLoader;
import ai.stapi.graphoperations.graphLoader.search.SearchOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AbstractOneValueFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.StartsWithFilterOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.List;
import java.util.Set;

public class InMemoryStartsWithFilterResolver extends InMemoryOneValueFilterResolver {

  public InMemoryStartsWithFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      InMemoryGraphLoader inMemoryGraphLoader
  ) {
    super(structureSchemaFinder, inMemoryGraphLoader);
  }

  @Override
  public boolean resolveValue(AbstractOneValueFilterOption<?> option, AttributeValue<?> actualValue) {
    if (actualValue instanceof StringLikeAttributeValue<?> stringLikeAttributeValue) {
      var stringFilterValue = (String) option.getParameters().getAttributeValue();
      return stringLikeAttributeValue.toStringValue().startsWith(stringFilterValue);
    }
    return false;
  }

  @Override
  protected boolean resolveValue(AbstractOneValueFilterOption<?> option, List<AttributeValue<?>> actualValue) {
    return false;
  }

  @Override
  protected boolean resolveValue(AbstractOneValueFilterOption<?> option, Set<AttributeValue<?>> actualValue) {
    return false;
  }

  @Override
  public boolean supports(SearchOption<?> option) {
    return option instanceof StartsWithFilterOption;
  }
}
