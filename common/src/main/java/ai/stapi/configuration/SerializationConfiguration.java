package ai.stapi.configuration;

import ai.stapi.serialization.classNameProvider.GenericClassNameProvider;
import ai.stapi.serialization.classNameProvider.specific.SpecificClassNameProvider;
import ai.stapi.serialization.jackson.SerializableObjectConfigurer;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class SerializationConfiguration {
  
  @Bean
  public GenericClassNameProvider genericClassNameProvider(
      List<SpecificClassNameProvider> specificClassNameProviders
  ) {
    return new GenericClassNameProvider(specificClassNameProviders);
  }
  
  @Bean
  public SerializableObjectConfigurer serializableObjectConfigurer(
      GenericClassNameProvider genericClassNameProvider
  ) {
    return new SerializableObjectConfigurer(genericClassNameProvider);
  }
}
