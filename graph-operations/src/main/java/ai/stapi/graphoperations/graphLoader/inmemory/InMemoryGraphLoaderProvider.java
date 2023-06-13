package ai.stapi.graphoperations.graphLoader.inmemory;

import ai.stapi.graph.Graph;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InMemoryGraphLoaderProvider {

  private final InMemoryGenericSearchOptionResolver searchOptionResolver;
  private final StructureSchemaFinder structureSchemaFinder;
  private final ObjectMapper objectMapper;

  public InMemoryGraphLoaderProvider(
      InMemoryGenericSearchOptionResolver searchOptionResolver,
      StructureSchemaFinder structureSchemaFinder, 
      ObjectMapper objectMapper
  ) {
    this.searchOptionResolver = searchOptionResolver;
    this.structureSchemaFinder = structureSchemaFinder;
    this.objectMapper = objectMapper;
  }

  public InMemoryGraphLoader provide(Graph graph) {
     return new InMemoryGraphLoader(
         graph,
         this.searchOptionResolver,
         this.structureSchemaFinder,
         this.objectMapper
     );
  }
}
