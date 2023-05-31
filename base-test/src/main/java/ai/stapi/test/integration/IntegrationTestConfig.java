package ai.stapi.test.integration;

import ai.stapi.schema.structureSchemaProvider.RestrictedStructureSchemaFinder;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import ai.stapi.schema.structuredefinition.loader.NullStructureDefinitionLoader;
import ai.stapi.schema.structuredefinition.loader.StructureDefinitionLoader;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;


@Profile("test")
@TestConfiguration
public class IntegrationTestConfig {

  @Bean
  public static StructureSchemaFinder structureSchemaFinder() {
    return new RestrictedStructureSchemaFinder();
  }

  @Bean
  public static StructureDefinitionLoader structureDefinitionLoader() {
    return new NullStructureDefinitionLoader();
  }
}
