package ai.stapi.graphoperations.configuration;

import ai.stapi.graphoperations.graphWriter.GenericGraphWriter;
import ai.stapi.graphoperations.objectGraphMapper.infrastructure.structureSchema.StructureSchemaInterfaceSpecificObjectGraphMapperFixStrategy;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.SpecificObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.specific.InterfaceSpecificObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.specific.InterfaceSpecificObjectGraphMapperFixStrategy;
import ai.stapi.graphoperations.objectGraphMapper.model.specific.LeafSpecificObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.specific.ListSpecificObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.specific.MapSpecificObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.specific.ObjectSpecificObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.specific.ReferenceSpecificObjectGraphMapper;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaProvider;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@AutoConfiguration
public class ObjectGraphMapperConfiguration {
  
  @Bean
  public GenericObjectGraphMapper genericObjectGraphMapper(
      @Lazy List<SpecificObjectGraphMapper> specificObjectGraphMappers,
      GenericGraphWriter writer,
      GenericGraphMappingProvider mappingProvider
  ) {
    return new GenericObjectGraphMapper(specificObjectGraphMappers, writer, mappingProvider);
  }
  
  @Bean
  public InterfaceSpecificObjectGraphMapper interfaceSpecificObjectGraphMapper(
      GenericObjectGraphMapper genericGraphMapper,
      GenericGraphMappingProvider mappingProvider,
      @Autowired(required = false)
      InterfaceSpecificObjectGraphMapperFixStrategy interfaceSpecificObjectGraphMapperFixStrategy
  ) {
    return new InterfaceSpecificObjectGraphMapper(
        genericGraphMapper,
        mappingProvider,
        interfaceSpecificObjectGraphMapperFixStrategy
    );
  }
  
  @Bean
  public LeafSpecificObjectGraphMapper leafSpecificObjectGraphMapper(
      GenericObjectGraphMapper genericObjectGraphMapper
  ) {
    return new LeafSpecificObjectGraphMapper(genericObjectGraphMapper);
  }
  
  @Bean
  public ListSpecificObjectGraphMapper listSpecificObjectGraphMapper(
      GenericObjectGraphMapper genericObjectGraphMapper
  ) {
    return new ListSpecificObjectGraphMapper(genericObjectGraphMapper);
  }

  @Bean
  public MapSpecificObjectGraphMapper mapSpecificObjectGraphMapper(
      GenericObjectGraphMapper genericObjectGraphMapper
  ) {
    return new MapSpecificObjectGraphMapper(genericObjectGraphMapper);
  }

  @Bean
  public ObjectSpecificObjectGraphMapper objectSpecificObjectGraphMapper(
      GenericObjectGraphMapper genericObjectGraphMapper
  ) {
    return new ObjectSpecificObjectGraphMapper(genericObjectGraphMapper);
  }

  @Bean
  public ReferenceSpecificObjectGraphMapper referenceSpecificObjectGraphMapper(
      GenericObjectGraphMapper genericObjectGraphMapper,
      GenericGraphMappingProvider mappingProvider
  ) {
    return new ReferenceSpecificObjectGraphMapper(genericObjectGraphMapper, mappingProvider);
  }
  
  @Bean
  public StructureSchemaInterfaceSpecificObjectGraphMapperFixStrategy structureSchemaInterfaceSpecificObjectGraphMapperFixStrategy(
      StructureSchemaProvider structureSchemaProvider
  ) {
    return new StructureSchemaInterfaceSpecificObjectGraphMapperFixStrategy(structureSchemaProvider);
  }
}
