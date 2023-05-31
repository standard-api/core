package ai.stapi.schema.configuration;

import ai.stapi.schema.scopeProvider.ScopeCacher;
import ai.stapi.schema.structureSchemaMapper.StructureDefinitionToSSMapper;
import ai.stapi.schema.structureSchemaProvider.DefaultStructureSchemaFinder;
import ai.stapi.schema.structureSchemaProvider.DefaultStructureSchemaProvider;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaProvider;
import ai.stapi.schema.structuredefinition.loader.StructureDefinitionLoader;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class StructureSchemaConfiguration {
  
  @Bean
  public StructureDefinitionToSSMapper structureDefinitionToSSMapper() {
    return new StructureDefinitionToSSMapper();
  }

  @Bean
  @ConditionalOnMissingBean
  public StructureSchemaProvider defaultStructureSchemaProvider(
      StructureDefinitionToSSMapper structureDefinitionToSSMapper,
      StructureDefinitionLoader structureDefinitionLoader,
      ScopeCacher scopeCacher
  ) {
    return new DefaultStructureSchemaProvider(
        structureDefinitionToSSMapper,
        structureDefinitionLoader,
        scopeCacher
    );
  }

  @Bean
  @ConditionalOnMissingBean
  public StructureSchemaFinder defaultStructureSchemaFinder(
      StructureSchemaProvider structureSchemaProvider
  ) {
    return new DefaultStructureSchemaFinder(structureSchemaProvider);
  }
}
