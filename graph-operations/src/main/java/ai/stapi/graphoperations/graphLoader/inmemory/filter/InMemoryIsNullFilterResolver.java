package ai.stapi.graphoperations.graphLoader.inmemory.filter;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGraphLoader;
import ai.stapi.graphoperations.graphLoader.search.SearchOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.IsNullFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.LeafFilterOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.List;
import java.util.Set;

public class InMemoryIsNullFilterResolver extends InMemoryLeafFilterResolver {

  public InMemoryIsNullFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      InMemoryGraphLoader inMemoryGraphLoader
  ) {
    super(structureSchemaFinder, inMemoryGraphLoader);
  }

  @Override
  public boolean resolve(LeafFilterOption<?> option, AttributeValue<?> actualValue) {
    return actualValue == null;
  }

  @Override
  protected boolean resolve(LeafFilterOption<?> option, List<AttributeValue<?>> actualValue) {
    return actualValue == null;
  }

  @Override
  protected boolean resolve(LeafFilterOption<?> option, Set<AttributeValue<?>> actualValue) {
    return actualValue == null;
  }

  @Override
  public boolean supports(SearchOption<?> option) {
    return option instanceof IsNullFilterOption;
  }
}
