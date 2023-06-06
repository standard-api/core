package ai.stapi.graphoperations.configuration;

import ai.stapi.graphoperations.graphReader.GraphReader;
import ai.stapi.graphoperations.graphReader.mappingPartReadResolvers.AttributeDescriptionReadResolver;
import ai.stapi.graphoperations.graphReader.mappingPartReadResolvers.AttributeValueDescriptionReadResolver;
import ai.stapi.graphoperations.graphReader.mappingPartReadResolvers.EdgeDescriptionReadResolver;
import ai.stapi.graphoperations.graphReader.mappingPartReadResolvers.GraphDescriptionReadResolver;
import ai.stapi.graphoperations.graphReader.mappingPartReadResolvers.NodeDescriptionReadResolver;
import ai.stapi.graphoperations.graphReader.mappingPartReadResolvers.UuidDescriptionReadResolver;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class GraphReaderConfiguration {
  
  @Bean
  public GraphReader graphReader(
      List<GraphDescriptionReadResolver> graphDescriptionResolvers
  ) {
    return new GraphReader(graphDescriptionResolvers);
  }
  
  @Bean
  public AttributeDescriptionReadResolver attributeDescriptionReadResolver() {
    return new AttributeDescriptionReadResolver();
  }
  
  @Bean
  public AttributeValueDescriptionReadResolver attributeValueDescriptionReadResolver() {
    return new AttributeValueDescriptionReadResolver();
  }
  
  @Bean
  public EdgeDescriptionReadResolver edgeDescriptionReadResolver() {
    return new EdgeDescriptionReadResolver();
  }
  
  @Bean
  public NodeDescriptionReadResolver nodeDescriptionReadResolver() {
    return new NodeDescriptionReadResolver();
  }
  
  @Bean
  public UuidDescriptionReadResolver uuidDescriptionReadResolver() {
    return new UuidDescriptionReadResolver();
  }
}
