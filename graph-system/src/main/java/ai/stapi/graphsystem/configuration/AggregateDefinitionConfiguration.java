package ai.stapi.graphsystem.configuration;

import ai.stapi.graphsystem.aggregatedefinition.infrastructure.AdHocAggregateDefinitionProvider;
import ai.stapi.graphsystem.aggregatedefinition.model.AggregateDefinitionProvider;
import ai.stapi.graphsystem.aggregatedefinition.model.ResourceAggregateDefinitionMapper;
import ai.stapi.graphsystem.aggregatedefinition.model.eventFactory.CreatedOperationEventFactoriesMapper;
import ai.stapi.graphsystem.aggregatedefinition.model.eventFactory.ItemAddedOperationEventFactoriesMapper;
import ai.stapi.graphsystem.operationdefinition.infrastructure.AdHocOperationDefinitionProvider;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionProvider;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionStructureTypeMapper;
import ai.stapi.schema.adHocLoaders.GenericAdHocModelDefinitionsLoader;
import ai.stapi.schema.scopeProvider.ScopeCacher;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@AutoConfiguration
public class AggregateDefinitionConfiguration {
  
  @Bean
  @ConditionalOnMissingBean(AggregateDefinitionProvider.class)
  public AdHocAggregateDefinitionProvider adHocAggregateDefinitionProvider(
      GenericAdHocModelDefinitionsLoader genericAdHocModelDefinitionsLoader,
      ScopeCacher scopeCacher,
      OperationDefinitionProvider operationDefinitionProvider
  ) {
    return new AdHocAggregateDefinitionProvider(
        genericAdHocModelDefinitionsLoader,
        scopeCacher,
        operationDefinitionProvider
    );
  }
  
  @Bean
  public ResourceAggregateDefinitionMapper resourceAggregateDefinitionMapper() {
    return new ResourceAggregateDefinitionMapper();
  }
  
  @Bean
  public CreatedOperationEventFactoriesMapper createdOperationEventFactoriesMapper(
      OperationDefinitionStructureTypeMapper operationDefinitionStructureTypeMapper,
      @Lazy AggregateDefinitionProvider aggregateDefinitionProvider
  ) {
    return new CreatedOperationEventFactoriesMapper(
        operationDefinitionStructureTypeMapper,
        aggregateDefinitionProvider
    );
  }
  
  @Bean
  public ItemAddedOperationEventFactoriesMapper itemAddedOperationEventFactoriesMapper(
      OperationDefinitionStructureTypeMapper operationDefinitionStructureTypeMapper
  ) {
    return new ItemAddedOperationEventFactoriesMapper(operationDefinitionStructureTypeMapper);
  }
}
