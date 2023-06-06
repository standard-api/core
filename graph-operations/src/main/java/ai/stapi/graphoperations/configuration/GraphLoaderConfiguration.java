package ai.stapi.graphoperations.configuration;

import ai.stapi.configuration.SerializationConfiguration;
import ai.stapi.graph.configuration.GraphRepositoryConfiguration;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graphoperations.graphLoader.GraphLoader;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGenericSearchOptionResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGraphLoader;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@AutoConfigureAfter({SerializationConfiguration.class, GraphRepositoryConfiguration.class})
public class GraphLoaderConfiguration {
  
  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(InMemoryGraphRepository.class)
  public GraphLoader inMemoryGraphLoader(
      InMemoryGraphRepository inMemoryGraphRepository,
      InMemoryGenericSearchOptionResolver inMemoryGenericSearchOptionResolver,
      StructureSchemaFinder structureSchemaFinder,
      ObjectMapper objectMapper
  ) {
    return new InMemoryGraphLoader(
        inMemoryGraphRepository,
        inMemoryGenericSearchOptionResolver,
        structureSchemaFinder,
        objectMapper
    );
  }
}
