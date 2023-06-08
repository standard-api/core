package ai.stapi.graphsystem.configuration;

import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import ai.stapi.graphsystem.genericGraphEventFactory.GenericGraphEventFactory;
import ai.stapi.graphsystem.structuredefinition.classnameprovider.StructureDefinitionClassNameProvider;
import ai.stapi.graphsystem.structuredefinition.command.importStructureDefinitionFromSource.ImportStructureDefinitionOgmProvider;
import ai.stapi.graphsystem.structuredefinition.command.importStructureDefinitionFromSource.StructureDefinitionImportedGraphEventFactory;
import ai.stapi.graphsystem.structuredefinition.identificatorProvider.ElementDefinitionIdentificatorProvider;
import ai.stapi.graphsystem.structuredefinition.identificatorProvider.StructureDefinitionDifferentialIdentificatorProvider;
import ai.stapi.graphsystem.structuredefinition.loader.SystemAdHocStructureDefinitionLoader;
import ai.stapi.schema.adHocLoaders.GenericAdHocModelDefinitionsLoader;
import ai.stapi.schema.scopeProvider.ScopeCacher;
import ai.stapi.schema.structuredefinition.loader.StructureDefinitionLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class StructureDefinitionConfiguration {
  
  @Bean
  @ConditionalOnMissingBean(StructureDefinitionLoader.class)
  public SystemAdHocStructureDefinitionLoader systemAdHocStructureDefinitionLoader(
      GenericAdHocModelDefinitionsLoader genericAdHocModelDefinitionsLoader,
      ObjectMapper objectMapper,
      ScopeCacher scopeCacher
  ) {
    return new SystemAdHocStructureDefinitionLoader(
        genericAdHocModelDefinitionsLoader,
        objectMapper,
        scopeCacher
    );
  }
  
  @Bean
  public ElementDefinitionIdentificatorProvider elementDefinitionIdentificatorProvider() {
    return new ElementDefinitionIdentificatorProvider();
  }
  
  @Bean
  public StructureDefinitionDifferentialIdentificatorProvider structureDefinitionDifferentialIdentificatorProvider() {
    return new StructureDefinitionDifferentialIdentificatorProvider();
  }
  
  @Bean
  public ImportStructureDefinitionOgmProvider importStructureDefinitionOgmProvider(
      GenericGraphMappingProvider genericGraphMappingProvider
  ) {
    return new ImportStructureDefinitionOgmProvider(genericGraphMappingProvider);
  }
  
  @Bean
  @ConditionalOnBean(GenericGraphEventFactory.class)
  public StructureDefinitionImportedGraphEventFactory structureDefinitionImportedGraphEventFactory() {
    return new StructureDefinitionImportedGraphEventFactory();
  }
  
  @Bean
  public StructureDefinitionClassNameProvider structureDefinitionClassNameProvider() {
    return new StructureDefinitionClassNameProvider();
  }
}
