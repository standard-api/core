package ai.stapi.graphoperations.graphLoader.search.filterOption;

import ai.stapi.graphoperations.graphLoader.search.AbstractSearchOptionResolver;
import ai.stapi.graphoperations.graphLoader.search.GenericSearchOptionResolver;
import ai.stapi.graphoperations.graphLoader.search.ResolvedQueryPart;
import ai.stapi.graphoperations.graphLoader.search.SearchResolvingContext;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;

public abstract class AbstractCompositeFilterOptionResolver<S extends CompositeFilterOption, C extends SearchResolvingContext, R extends ResolvedQueryPart>
    extends AbstractSearchOptionResolver<S, C, R> {

  private final GenericSearchOptionResolver<R> genericSearchOptionResolver;

  protected AbstractCompositeFilterOptionResolver(
      StructureSchemaFinder structureSchemaFinder,
      GenericSearchOptionResolver<R> genericSearchOptionResolver
  ) {
    super(structureSchemaFinder);
    this.genericSearchOptionResolver = genericSearchOptionResolver;
  }

  protected abstract R reduceChildResolvedFilters(R reduced, R childResolvedFilter);

  protected abstract SearchResolvingContext createCompositeChildContext(C parentContext);

  protected abstract SearchResolvingContext createLeafChildContext(C parentContext,
      Integer leafIndex);

  protected R postProcessResolvedFilter(R resolvedFilter, C context) {
    return resolvedFilter;
  }

  @Override
  protected R resolveTyped(S option, C context) {
    var leafOptionCounter = 0;
    var childOptions = option.getParameters().getChildFilterOptions();
    R reducedResolvedFilter;
    if (childOptions.get(0) instanceof CompositeFilterOption) {
      reducedResolvedFilter = this.genericSearchOptionResolver.resolve(
          childOptions.get(0),
          this.createCompositeChildContext(context)
      );
    } else {
      reducedResolvedFilter = this.genericSearchOptionResolver.resolve(
          childOptions.get(0),
          this.createLeafChildContext(context, leafOptionCounter)
      );
      leafOptionCounter++;
    }
    for (var childOption : childOptions.subList(1, childOptions.size())) {
      R childResolvedFilter;
      if (childOption instanceof CompositeFilterOption) {
        childResolvedFilter = this.genericSearchOptionResolver.resolve(
            childOption,
            this.createCompositeChildContext(context)
        );
      } else {
        childResolvedFilter = this.genericSearchOptionResolver.resolve(
            childOption,
            this.createLeafChildContext(context, leafOptionCounter)
        );
        leafOptionCounter++;
      }
      reducedResolvedFilter =
          this.reduceChildResolvedFilters(reducedResolvedFilter, childResolvedFilter);
    }
    return this.postProcessResolvedFilter(reducedResolvedFilter, context);
  }

}
