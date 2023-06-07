package ai.stapi.graphsystem.configuration;

import ai.stapi.graphsystem.operationdefinition.infrastructure.AdHocOperationDefinitionProvider;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionProvider;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionStructureTypeMapper;
import ai.stapi.graphsystem.operationdefinition.model.resourceStructureTypeOperationsMapper.AddItemOnResourceOperationsMapper;
import ai.stapi.graphsystem.operationdefinition.model.resourceStructureTypeOperationsMapper.CreationalResourceOperationMapper;
import ai.stapi.schema.adHocLoaders.GenericAdHocModelDefinitionsLoader;
import ai.stapi.schema.scopeProvider.ScopeCacher;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class OperationDefinitionConfiguration {
  
  @Bean
  @ConditionalOnMissingBean(OperationDefinitionProvider.class)
  public AdHocOperationDefinitionProvider adHocOperationDefinitionProvider(
      GenericAdHocModelDefinitionsLoader genericAdHocModelDefinitionsLoader,
      ScopeCacher scopeCacher
  ) {
    return new AdHocOperationDefinitionProvider(
        genericAdHocModelDefinitionsLoader,
        scopeCacher
    );
  }
  
  @Bean
  public OperationDefinitionStructureTypeMapper operationDefinitionStructureTypeMapper(
      StructureSchemaProvider structureSchemaProvider
  ) {
    return new OperationDefinitionStructureTypeMapper(structureSchemaProvider);
  }
  
  @Bean
  public AddItemOnResourceOperationsMapper addItemOnResourceOperationsMapper(
      StructureSchemaFinder structureSchemaFinder
  ) {
    return new AddItemOnResourceOperationsMapper(structureSchemaFinder);
  }
  
  @Bean
  public CreationalResourceOperationMapper creationalResourceOperationMapper(
      StructureSchemaFinder structureSchemaFinder
  ) {
    return new CreationalResourceOperationMapper(structureSchemaFinder);
  }
}
