package ai.stapi.graphoperations.graphLoader.inmemory.filter;

import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemorySearchResolvingContext;
import ai.stapi.graphoperations.graphLoader.search.SearchOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.FilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.NotFilterOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;

public class InMemoryNotFilterResolver extends InMemoryFilterResolver {

  private final GenericInMemoryFilterOptionResolver genericInMemoryFilterOptionResolver;

  public InMemoryNotFilterResolver(
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
    var notFilter = (NotFilterOption) option;
    return !this.genericInMemoryFilterOptionResolver.resolveElement(
        notFilter.getChildFilterOption(), 
        element,
        context
    );
  }

  @Override
  public boolean supports(SearchOption<?> option) {
    return option instanceof NotFilterOption;
  }
}
