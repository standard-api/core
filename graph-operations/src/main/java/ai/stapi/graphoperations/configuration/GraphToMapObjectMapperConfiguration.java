package ai.stapi.graphoperations.configuration;

import ai.stapi.graphoperations.graphReader.GraphReader;
import ai.stapi.graphoperations.graphToMapObjectMapper.GraphToMapObjectMapper;
import ai.stapi.graphoperations.graphToMapObjectMapper.specific.InterfaceGraphToMapMapper;
import ai.stapi.graphoperations.graphToMapObjectMapper.specific.LeafGraphToMapMapper;
import ai.stapi.graphoperations.graphToMapObjectMapper.specific.ListGraphToMapMapper;
import ai.stapi.graphoperations.graphToMapObjectMapper.specific.ObjectGraphToMapMapper;
import ai.stapi.graphoperations.graphToMapObjectMapper.specific.ReferenceGraphToMapMapper;
import ai.stapi.graphoperations.graphToMapObjectMapper.specific.SpecificGraphToMapMapper;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@AutoConfiguration
public class GraphToMapObjectMapperConfiguration {
  
  @Bean
  public GraphToMapObjectMapper graphToMapObjectMapper(
      @Lazy List<SpecificGraphToMapMapper> specificGraphToMapMappers,
      GenericGraphMappingProvider genericGraphMappingProvider
  ) {
    return new GraphToMapObjectMapper(specificGraphToMapMappers, genericGraphMappingProvider);
  }
  
  @Bean
  public InterfaceGraphToMapMapper interfaceGraphToMapMapper(
      GraphReader graphReader,
      GraphToMapObjectMapper graphToMapObjectMapper,
      GenericGraphMappingProvider genericGraphMappingProvider
  ) {
    return new InterfaceGraphToMapMapper(graphReader, graphToMapObjectMapper, genericGraphMappingProvider);
  }

  @Bean
  public LeafGraphToMapMapper leafGraphToMapMapper(
      GraphReader graphReader,
      GraphToMapObjectMapper graphToMapObjectMapper,
      GenericGraphMappingProvider genericGraphMappingProvider
  ) {
    return new LeafGraphToMapMapper(graphReader, graphToMapObjectMapper, genericGraphMappingProvider);
  }

  @Bean
  public ListGraphToMapMapper listGraphToMapMapper(
      GraphReader graphReader,
      GraphToMapObjectMapper graphToMapObjectMapper,
      GenericGraphMappingProvider genericGraphMappingProvider
  ) {
    return new ListGraphToMapMapper(graphReader, graphToMapObjectMapper, genericGraphMappingProvider);
  }

  @Bean
  public ObjectGraphToMapMapper objectGraphToMapMapper(
      GraphReader graphReader,
      GraphToMapObjectMapper graphToMapObjectMapper,
      GenericGraphMappingProvider genericGraphMappingProvider
  ) {
    return new ObjectGraphToMapMapper(graphReader, graphToMapObjectMapper, genericGraphMappingProvider);
  }

  @Bean
  public ReferenceGraphToMapMapper referenceGraphToMapMapper(
      GraphReader graphReader,
      GraphToMapObjectMapper graphToMapObjectMapper,
      GenericGraphMappingProvider genericGraphMappingProvider
  ) {
    return new ReferenceGraphToMapMapper(graphReader, graphToMapObjectMapper, genericGraphMappingProvider);
  }
}
