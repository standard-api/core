package ai.stapi.graphoperations.configuration;

import ai.stapi.graphoperations.graphLanguage.classnameprovider.GraphDescriptionClassNameProvider;
import ai.stapi.graphoperations.objectGraphLanguage.classnameprovider.ObjectGraphMappingClassNameProvider;
import ai.stapi.graphoperations.objectLanguage.classnameprovider.ObjectLanguageClassNameProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ClassNameProviderConfiguration {
  
  @Bean
  public GraphDescriptionClassNameProvider graphDescriptionClassNameProvider() {
    return new GraphDescriptionClassNameProvider();
  }
  
  @Bean
  public ObjectGraphMappingClassNameProvider objectGraphMappingClassNameProvider() {
    return new ObjectGraphMappingClassNameProvider();
  }
  
  @Bean
  public ObjectLanguageClassNameProvider objectLanguageClassNameProvider() {
    return new ObjectLanguageClassNameProvider();
  }
}
