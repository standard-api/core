package ai.stapi.graphoperations.graphLoader.inmemory.filter;

import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphLoader.inmemory.AbstractInMemorySearchOptionResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemorySearchResolvingContext;
import ai.stapi.graphoperations.graphLoader.search.filterOption.FilterOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;

public abstract class InMemoryFilterResolver extends AbstractInMemorySearchOptionResolver<FilterOption<?>> {

  protected InMemoryFilterResolver(
      StructureSchemaFinder structureSchemaFinder
  ) {
    super(structureSchemaFinder);
  }

  public abstract boolean resolveElement(
      FilterOption<?> option,
      TraversableGraphElement element,
      InMemorySearchResolvingContext context
  );

  @Override
  protected InMemorySearchResolvingContext resolveTyped(
      FilterOption<?> option,
      InMemorySearchResolvingContext context
  ) {
    return context.setSearchOption(
        elements -> elements.filter(element -> this.resolveElement(option, element, context))
    );
  }
}
