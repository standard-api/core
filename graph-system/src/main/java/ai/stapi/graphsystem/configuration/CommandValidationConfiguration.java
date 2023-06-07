package ai.stapi.graphsystem.configuration;

import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.GenericAttributeValueFactory;
import ai.stapi.graphsystem.commandvalidation.infrastructure.operationdefinition.OperationDefinitionCommandValidator;
import ai.stapi.graphsystem.commandvalidation.model.CommandValidator;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionProvider;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionStructureTypeMapper;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class CommandValidationConfiguration {
  
  @Bean
  @ConditionalOnMissingBean(CommandValidator.class)
  public OperationDefinitionCommandValidator operationDefinitionCommandValidator(
      OperationDefinitionProvider operationDefinitionProvider,
      StructureSchemaProvider structureSchemaProvider,
      OperationDefinitionStructureTypeMapper operationDefinitionStructureTypeMapper,
      GenericAttributeValueFactory genericAttributeValueFactory,
      ObjectMapper objectMapper
  ) {
    return new OperationDefinitionCommandValidator(
        operationDefinitionProvider,
        structureSchemaProvider,
        operationDefinitionStructureTypeMapper,
        genericAttributeValueFactory,
        objectMapper
    );
  }
}
