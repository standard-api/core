package ai.stapi.graphoperations.graphLoader.inmemory.filter;

import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemorySearchResolvingContext;
import ai.stapi.graphoperations.graphLoader.search.SearchOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.FilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.OrFilterOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import org.springframework.stereotype.Service;

@Service
public class InMemoryOrFilterResolver extends InMemoryFilterResolver {

  private final GenericInMemoryFilterOptionResolver genericInMemoryFilterOptionResolver;

  protected InMemoryOrFilterResolver(
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
    var orFilter = (OrFilterOption) option;
    return orFilter.getParameters().getChildFilterOptions().stream().anyMatch(
        fiter -> this.genericInMemoryFilterOptionResolver.resolveElement(fiter, element, context)
    );
  }

  @Override
  public boolean supports(SearchOption<?> option) {
    return option instanceof OrFilterOption;
  }
}
