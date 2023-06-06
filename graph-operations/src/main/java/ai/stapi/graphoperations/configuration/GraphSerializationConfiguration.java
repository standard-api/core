package ai.stapi.graphoperations.configuration;

import ai.stapi.graph.attribute.attributeFactory.GenericAttributeFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.GenericAttributeValueFactory;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.GenericGraphToObjectDeserializer;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.specific.InterfaceGraphToObjectDeserializer;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.specific.LeafGraphToObjectDeserializer;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.specific.ListGraphToObjectDeserializer;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.specific.MapGraphToObjectDeserializer;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.specific.ObjectGraphToObjectDeserializer;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.specific.SpecificGraphToObjectDeserializer;
import ai.stapi.graphoperations.graphReader.GraphReader;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import ai.stapi.graphoperations.serializableGraph.deserializer.SerializableAttributeDeserializer;
import ai.stapi.graphoperations.serializableGraph.deserializer.SerializableEdgeDeserializer;
import ai.stapi.graphoperations.serializableGraph.deserializer.SerializableGraphDeserializer;
import ai.stapi.graphoperations.serializableGraph.deserializer.SerializableNodeDeserializer;
import ai.stapi.graphoperations.serializableGraph.jackson.SerializableGraphConfigurer;
import ai.stapi.graphoperations.serializationTypeProvider.GenericSerializationTypeByNodeProvider;
import ai.stapi.graphoperations.serializationTypeProvider.specific.ByMapSerializationTypeProvider;
import ai.stapi.graphoperations.serializationTypeProvider.specific.SpecificSerializationTypeProvider;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@AutoConfiguration
public class GraphSerializationConfiguration {
  
  @Bean
  public SerializableGraphConfigurer serializableGraphConfigurer(
      SerializableGraphDeserializer serializableGraphDeserializer
  ) {
    return new SerializableGraphConfigurer(serializableGraphDeserializer);
  }
  
  @Bean
  public SerializableGraphDeserializer serializableGraphDeserializer(
      SerializableNodeDeserializer serializableNodeDeserializer,
      SerializableEdgeDeserializer serializableEdgeDeserializer
  ) {
    return new SerializableGraphDeserializer(serializableNodeDeserializer, serializableEdgeDeserializer);
  }
  
  @Bean
  public SerializableNodeDeserializer serializableNodeDeserializer(
      SerializableAttributeDeserializer serializableAttributeDeserializer
  ) {
    return new SerializableNodeDeserializer(serializableAttributeDeserializer);
  }
  
  @Bean
  public SerializableEdgeDeserializer serializableEdgeDeserializer(
      SerializableAttributeDeserializer serializableAttributeDeserializer
  ) {
    return new SerializableEdgeDeserializer(serializableAttributeDeserializer);
  }
  
  @Bean
  public SerializableAttributeDeserializer serializableAttributeDeserializer(
      StructureSchemaFinder structureSchemaFinder,
      GenericAttributeFactory genericAttributeFactory,
      GenericAttributeValueFactory genericAttributeValueFactory
  ) {
    return new SerializableAttributeDeserializer(
        structureSchemaFinder, 
        genericAttributeFactory, 
        genericAttributeValueFactory
    );
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
  
  @Bean
  public GenericGraphToObjectDeserializer genericGraphToObjectDeserializer(
      @Lazy List<SpecificGraphToObjectDeserializer> specificGraphToObjectDeserializers,
      GraphReader graphReader,
      ObjectMapper objectMapper,
      GenericGraphMappingProvider genericGraphMappingProvider
  ) {
    return new GenericGraphToObjectDeserializer(
        specificGraphToObjectDeserializers,
        graphReader,
        objectMapper,
        genericGraphMappingProvider
    );
  }
  
  @Bean
  public InterfaceGraphToObjectDeserializer interfaceGraphToObjectDeserializer(
      GraphReader graphReader,
      GenericGraphToObjectDeserializer genericDeserializer,
      GenericSerializationTypeByNodeProvider serializationTypeProvider,
      GenericGraphMappingProvider mappingProvider
  ) {
    return new InterfaceGraphToObjectDeserializer(
        graphReader,
        genericDeserializer,
        serializationTypeProvider,
        mappingProvider
    );
  }
  
  @Bean
  public LeafGraphToObjectDeserializer leafGraphToObjectDeserializer(
      GraphReader graphReader,
      GenericGraphToObjectDeserializer genericDeserializer,
      GenericSerializationTypeByNodeProvider serializationTypeProvider,
      GenericGraphMappingProvider mappingProvider
  ) {
    return new LeafGraphToObjectDeserializer(
        graphReader,
        genericDeserializer,
        serializationTypeProvider,
        mappingProvider
    );
  }

  @Bean
  public ListGraphToObjectDeserializer listGraphToObjectDeserializer(
      GraphReader graphReader,
      GenericGraphToObjectDeserializer genericDeserializer,
      GenericSerializationTypeByNodeProvider serializationTypeProvider,
      GenericGraphMappingProvider mappingProvider
  ) {
    return new ListGraphToObjectDeserializer(
        graphReader,
        genericDeserializer,
        serializationTypeProvider,
        mappingProvider
    );
  }

  @Bean
  public MapGraphToObjectDeserializer mapGraphToObjectDeserializer(
      GraphReader graphReader,
      GenericGraphToObjectDeserializer genericDeserializer,
      GenericSerializationTypeByNodeProvider serializationTypeProvider,
      GenericGraphMappingProvider mappingProvider
  ) {
    return new MapGraphToObjectDeserializer(
        graphReader,
        genericDeserializer,
        serializationTypeProvider,
        mappingProvider
    );
  }

  @Bean
  public ObjectGraphToObjectDeserializer objectGraphToObjectDeserializer(
      GraphReader graphReader,
      GenericGraphToObjectDeserializer genericDeserializer,
      GenericSerializationTypeByNodeProvider serializationTypeProvider,
      GenericGraphMappingProvider mappingProvider
  ) {
    return new ObjectGraphToObjectDeserializer(
        graphReader,
        genericDeserializer,
        serializationTypeProvider,
        mappingProvider
    );
  }
  
  @Bean
  public GenericSerializationTypeByNodeProvider genericSerializationTypeByNodeProvider(
      List<SpecificSerializationTypeProvider> specificSerializationTypeProviders
  ) {
    return new GenericSerializationTypeByNodeProvider(specificSerializationTypeProviders);
  }
  
  @Bean
  public ByMapSerializationTypeProvider byMapSerializationTypeProvider() {
    return new ByMapSerializationTypeProvider();
  }
}
