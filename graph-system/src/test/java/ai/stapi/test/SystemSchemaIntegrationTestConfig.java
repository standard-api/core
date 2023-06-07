package ai.stapi.test;

import ai.stapi.graphsystem.structuredefinition.loader.SystemAdHocStructureDefinitionLoader;
import ai.stapi.schema.structuredefinition.loader.StructureDefinitionLoader;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;


@Profile("test")
@TestConfiguration
public class SystemSchemaIntegrationTestConfig {

  @Bean
  @Primary
  public static StructureDefinitionLoader systemStructureDefinitionLoader(
      SystemAdHocStructureDefinitionLoader structureDefinitionLoader
  ) {
    return structureDefinitionLoader;
  }
}
