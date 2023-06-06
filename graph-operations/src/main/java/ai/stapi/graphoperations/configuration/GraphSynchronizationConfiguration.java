package ai.stapi.graphoperations.configuration;

import ai.stapi.graph.EdgeRepository;
import ai.stapi.graph.NodeRepository;
import ai.stapi.graphoperations.graphLoader.GraphLoader;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGenericSearchOptionResolver;
import ai.stapi.graphoperations.graphReader.GraphReader;
import ai.stapi.graphoperations.synchronization.GraphSynchronizer;
import ai.stapi.graphoperations.synchronization.IdentifyingGraphSynchronizer;
import ai.stapi.graphoperations.synchronization.nodeIdentificator.DefaultUuidIdentityIdentificatorProvider;
import ai.stapi.graphoperations.synchronization.nodeIdentificator.GenericNodeIdentificatorsProvider;
import ai.stapi.graphoperations.synchronization.nodeIdentificator.NodeIdentificatorsProvider;
import ai.stapi.graphoperations.synchronization.nodeIdentificator.NodeIdentifyingFiltersResolver;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@AutoConfiguration
public class GraphSynchronizationConfiguration {
  
  @Bean
  @ConditionalOnMissingBean
  public GraphSynchronizer identifyingGraphSynchronizer(
      @Lazy NodeRepository nodeRepository,
      @Lazy EdgeRepository edgeRepository,
      NodeIdentifyingFiltersResolver nodeIdentifyingFiltersResolver,
      GraphLoader graphLoader,
      InMemoryGenericSearchOptionResolver searchOptionResolver,
      ObjectMapper objectMapper,
      StructureSchemaFinder structureSchemaFinder
  ) {
    return new IdentifyingGraphSynchronizer(
        nodeRepository,
        edgeRepository,
        nodeIdentifyingFiltersResolver,
        graphLoader,
        searchOptionResolver,
        objectMapper,
        structureSchemaFinder
    );
  }

  @Bean
  public GenericNodeIdentificatorsProvider genericNodeIdentificatorsProvider(
      List<NodeIdentificatorsProvider> nodeIdentificatorsProviders
  ) {
    return new GenericNodeIdentificatorsProvider(nodeIdentificatorsProviders);
  }
  
  @Bean
  public DefaultUuidIdentityIdentificatorProvider defaultUuidIdentityIdentificatorProvider() {
    return new DefaultUuidIdentityIdentificatorProvider();
  }
  
  @Bean
  public NodeIdentifyingFiltersResolver nodeIdentifyingFiltersResolver(
      GenericNodeIdentificatorsProvider genericNodeIdentificatorsProvider,
      GraphReader graphReader
  ) {
    return new NodeIdentifyingFiltersResolver(genericNodeIdentificatorsProvider, graphReader);
  }
}
