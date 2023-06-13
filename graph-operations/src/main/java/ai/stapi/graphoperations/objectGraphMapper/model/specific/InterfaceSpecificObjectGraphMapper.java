package ai.stapi.graphoperations.objectGraphMapper.model.specific;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import ai.stapi.graphoperations.objectGraphLanguage.InterfaceObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.graphoperations.objectGraphMapper.model.specific.exceptions.SpecificObjectGraphMapperException;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import ai.stapi.serialization.AbstractSerializableObject;
import ai.stapi.serialization.SerializableObject;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public class InterfaceSpecificObjectGraphMapper extends AbstractSpecificObjectGraphMapper {

  private final GenericGraphMappingProvider mappingProvider;
  
  @Nullable
  private final InterfaceSpecificObjectGraphMapperFixStrategy interfaceSpecificObjectGraphMapperFixStrategy;

  public InterfaceSpecificObjectGraphMapper(
      GenericObjectGraphMapper genericGraphMapper,
      GenericGraphMappingProvider mappingProvider,
      @Nullable InterfaceSpecificObjectGraphMapperFixStrategy interfaceSpecificObjectGraphMapperFixStrategy
  ) {
    super(genericGraphMapper);
    this.mappingProvider = mappingProvider;
    this.interfaceSpecificObjectGraphMapperFixStrategy = interfaceSpecificObjectGraphMapperFixStrategy;
  }

  @Override
  public GraphDescriptionBuilder createGraphDescriptionWithValues(
      ObjectGraphMapping objectGraphMapping,
      Map.Entry<String, Object> fieldEntry,
      GraphDescriptionBuilder builder,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  ) {
    String serializationType;
    if (fieldEntry.getValue() instanceof Map map) {
      serializationType = this.getSerializationTypeFromMap(map);
    } else if (fieldEntry.getValue() instanceof SerializableObject serializableObject) {
      serializationType = serializableObject.getSerializationType();
    } else {
      throw SpecificObjectGraphMapperException.becauseActualEntityBehindInterfaceIsNotObject(
          fieldEntry.getValue()
      );
    }
    if (this.isNullDeclaration(serializationType)) {
      return builder;
    }
    if (serializationType.isBlank()) {
      throw SpecificObjectGraphMapperException.becauseSerializationTypeWasNotFound(
          fieldEntry.getKey(), 
          fieldEntry.getValue()
      );
    }
    var mappingDefinition = this.mappingProvider.provideGraphMapping(
        serializationType, 
        fieldEntry.getKey()
    );
    var interfaceObjectGraphMapping = (InterfaceObjectGraphMapping) objectGraphMapping;
    if (this.interfaceSpecificObjectGraphMapperFixStrategy != null) {
      interfaceObjectGraphMapping = this.interfaceSpecificObjectGraphMapperFixStrategy.fix(
          interfaceObjectGraphMapping,
          fieldEntry,
          builder,
          serializationType
      );
    }
    var resolvedBuilder = this.addGraphDescriptionCompositeToBuilder(
        interfaceObjectGraphMapping.getGraphDescription(),
        builder
    );
    this.genericGraphMapper.resolveInternally(
        mappingDefinition,
        fieldEntry,
        resolvedBuilder,
        missingFieldResolvingStrategy
    );
    return resolvedBuilder;
  }

  private String getSerializationTypeFromMap(Map map) {
    return (String) map.getOrDefault(
        AbstractSerializableObject.NAME_OF_FIELD_WITH_SERIALIZATION_TYPE,
        ""
    );
  }

  @Override
  public boolean supports(ObjectGraphMapping objectGraphMapping) {
    return objectGraphMapping instanceof InterfaceObjectGraphMapping;
  }

  private boolean isNullDeclaration(String serializationType) {
    return serializationType.equals(NullGraphDescription.SERIALIZATION_TYPE);
  }
}
