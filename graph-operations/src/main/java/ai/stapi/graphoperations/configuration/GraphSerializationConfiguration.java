package ai.stapi.graphoperations.configuration;

import ai.stapi.graphoperations.serializableGraph.deserializer.SerializableGraphDeserializer;
import ai.stapi.graphoperations.serializableGraph.jackson.SerializableGraphConfigurer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class GraphSerializationConfiguration {
  
  @Bean
  public SerializableGraphConfigurer serializableGraphConfigurer(
      SerializableGraphDeserializer serializableGraphDeserializer
  ) {
    return new SerializableGraphConfigurer(serializableGraphDeserializer);
  }
  
  @Bean
  @ConditionalOnBean
  public ObjectMapper graphOperationsObjectMapper(
      ObjectMapper objectMapper,
      SerializableGraphConfigurer serializableGraphConfigurer
  ) {
    serializableGraphConfigurer.configure(objectMapper);
    return objectMapper;
  }
}
