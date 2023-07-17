package ai.stapi.configuration;

import ai.stapi.serialization.classNameProvider.GenericClassNameProvider;
import ai.stapi.serialization.classNameProvider.specific.SpecificClassNameProvider;
import ai.stapi.serialization.jackson.JavaTimeConfigurer;
import ai.stapi.serialization.jackson.SerializableObjectConfigurer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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

  @Bean
  @ConditionalOnMissingBean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
  
  @Bean
  @ConditionalOnBean(ObjectMapper.class)
  public ObjectMapper commonObjectMapper(
      ObjectMapper objectMapper,
      SerializableObjectConfigurer serializableObjectConfigurer
  ) {
    serializableObjectConfigurer.configure(objectMapper);
    JavaTimeConfigurer.configureJavaTimeModule(objectMapper);
    return objectMapper;
  }
}
