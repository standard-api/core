package ai.stapi.graphoperations.graphLoader.inmemory;

import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graphoperations.graphLoader.search.SearchOption;
import ai.stapi.graphoperations.graphLoader.search.sortOption.DescendingSortOption;
import ai.stapi.graphoperations.graphLoader.search.sortOption.SortOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;

public class InMemoryDescendingSortResolver extends InMemorySortResolver {

  public InMemoryDescendingSortResolver(
      StructureSchemaFinder structureSchemaFinder,
      InMemoryGraphLoader inMemoryGraphLoader
  ) {
    super(structureSchemaFinder, inMemoryGraphLoader);
  }

  @Override
  protected InMemorySearchResolvingContext resolveTyped(
      SortOption option,
      InMemorySearchResolvingContext context
  ) {
    var descending = (DescendingSortOption) option;
    return context.setDescSort(
        graphElement -> this.getSortingValue(
            descending, 
            graphElement, 
            new BooleanAttributeValue(false),
            context
        )
    );
  }

  @Override
  public boolean supports(SearchOption<?> option) {
    return option instanceof DescendingSortOption;
  }
}
