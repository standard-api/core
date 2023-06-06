package ai.stapi.graphoperations.graphLoader.inmemory;

import ai.stapi.graphoperations.graphLoader.search.AbstractSearchOptionResolver;
import ai.stapi.graphoperations.graphLoader.search.SearchOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;

public abstract class AbstractInMemorySearchOptionResolver<S extends SearchOption<?>>
    extends AbstractSearchOptionResolver<S, InMemorySearchResolvingContext, InMemorySearchResolvingContext> {

  protected AbstractInMemorySearchOptionResolver(
      StructureSchemaFinder structureSchemaFinder
  ) {
    super(structureSchemaFinder);
  }
}
