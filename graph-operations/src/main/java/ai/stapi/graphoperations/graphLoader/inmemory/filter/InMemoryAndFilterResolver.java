package ai.stapi.graphoperations.graphLoader.inmemory.filter;

import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemorySearchResolvingContext;
import ai.stapi.graphoperations.graphLoader.search.SearchOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AndFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.FilterOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;

public class InMemoryAndFilterResolver extends InMemoryFilterResolver {

  private final GenericInMemoryFilterOptionResolver genericInMemoryFilterOptionResolver;

  public InMemoryAndFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      GenericInMemoryFilterOptionResolver genericInMemoryFilterOptionResolver
  ) {
    super(structureSchemaFinder);
    this.genericInMemoryFilterOptionResolver = genericInMemoryFilterOptionResolver;
  }


  @Override
  public boolean resolveElement(
      FilterOption<?> option, 
      TraversableGraphElement element,
      InMemorySearchResolvingContext context
  ) {
    var andFilter = (AndFilterOption) option;
    return andFilter.getParameters().getChildFilterOptions().stream().allMatch(
        fiter -> this.genericInMemoryFilterOptionResolver.resolveElement(fiter, element, context)
    );
  }

  @Override
  public boolean supports(SearchOption<?> option) {
    return option instanceof AndFilterOption;
  }
}
