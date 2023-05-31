package ai.stapi.schema.configuration;

import ai.stapi.schema.adHocLoaders.FileLoader;
import ai.stapi.schema.adHocLoaders.GenericAdHocModelDefinitionsLoader;
import ai.stapi.schema.adHocLoaders.SpecificAdHocModelDefinitionsLoader;
import ai.stapi.schema.scopeProvider.ScopeCacher;
import ai.stapi.schema.structuredefinition.loader.AdHocStructureDefinitionLoader;
import ai.stapi.schema.structuredefinition.loader.StructureDefinitionLoader;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class AdHocLoaderConfiguration {
  
  @Bean
  public FileLoader fileLoader() {
    return new FileLoader();
  }
  
  @Bean
  public GenericAdHocModelDefinitionsLoader genericAdHocModelDefinitionsLoader(
      List<SpecificAdHocModelDefinitionsLoader> specificAdHocModelDefinitionsLoaders
  ) {
    return new GenericAdHocModelDefinitionsLoader(specificAdHocModelDefinitionsLoaders);
  }
  
  @Bean
  @ConditionalOnMissingBean
  public StructureDefinitionLoader adHocStructureDefinitionLoader(
      GenericAdHocModelDefinitionsLoader genericAdHocModelDefinitionsLoader,
      ScopeCacher scopeCacher
  ) {
    return new AdHocStructureDefinitionLoader(genericAdHocModelDefinitionsLoader, scopeCacher);
  }
}
