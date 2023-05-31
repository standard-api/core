package ai.stapi.test.schemaintegration;

import ai.stapi.schema.adHocLoaders.GenericAdHocModelDefinitionsLoader;
import ai.stapi.schema.scopeProvider.ScopeCacher;
import ai.stapi.schema.structureSchemaProvider.DefaultStructureSchemaFinder;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaProvider;
import ai.stapi.schema.structuredefinition.loader.AdHocStructureDefinitionLoader;
import ai.stapi.schema.structuredefinition.loader.StructureDefinitionLoader;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;


@Profile("test")
@TestConfiguration
public class SchemaIntegrationTestConfig {

  @Bean
  public static StructureDefinitionLoader structureDefinitionLoader(
      GenericAdHocModelDefinitionsLoader genericAdHocModelDefinitionsLoader,
      ScopeCacher scopeCacher
  ) {
    return new AdHocStructureDefinitionLoader(genericAdHocModelDefinitionsLoader, scopeCacher);
  }

  @Bean
  public static StructureSchemaFinder structureSchemaFinder(
      StructureSchemaProvider structureSchemaProvider
  ) {
    return new DefaultStructureSchemaFinder(structureSchemaProvider);
  }
}
