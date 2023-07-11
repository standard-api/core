package ai.stapi.formapi.configuration;

import ai.stapi.formapi.formmapper.JsonSchemaMapper;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionStructureTypeMapper;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ComponentScan("ai.stapi.formapi")
public class FormApiConfiguration {
  
  @Bean
  public JsonSchemaMapper jsonSchemaMapper(
      OperationDefinitionStructureTypeMapper operationDefinitionStructureTypeMapper,
      StructureSchemaFinder structureSchemaFinder
  ) {
    return new JsonSchemaMapper(operationDefinitionStructureTypeMapper, structureSchemaFinder);
  }
}
