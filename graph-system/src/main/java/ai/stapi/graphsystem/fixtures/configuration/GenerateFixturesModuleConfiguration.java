package ai.stapi.graphsystem.fixtures.configuration;

import ai.stapi.graphsystem.aggregatedefinition.infrastructure.AdHocAggregateDefinitionProvider;
import ai.stapi.graphsystem.aggregatedefinition.model.AggregateDefinitionProvider;
import ai.stapi.graphsystem.structuredefinition.loader.SystemAdHocStructureDefinitionLoader;
import ai.stapi.schema.structuredefinition.loader.StructureDefinitionLoader;
import ai.stapi.graphsystem.operationdefinition.infrastructure.AdHocOperationDefinitionProvider;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("generate-fixtures")
public class GenerateFixturesModuleConfiguration {

  @Bean
  public StructureDefinitionLoader structureDefinitionLoader(
      SystemAdHocStructureDefinitionLoader systemAdHocStructureDefinitionLoader
  ) {
    return systemAdHocStructureDefinitionLoader;
  }

  @Bean
  public OperationDefinitionProvider operationDefinitionProvider(
      @Autowired AdHocOperationDefinitionProvider adHocOperationDefinitionProvider
  ) {
    return adHocOperationDefinitionProvider;
  }

  @Bean
  public AggregateDefinitionProvider aggregateDefinitionProvider(
      @Autowired AdHocAggregateDefinitionProvider adHocAggregateDefinitionProvider
  ) {
    return adHocAggregateDefinitionProvider;
  }
}
