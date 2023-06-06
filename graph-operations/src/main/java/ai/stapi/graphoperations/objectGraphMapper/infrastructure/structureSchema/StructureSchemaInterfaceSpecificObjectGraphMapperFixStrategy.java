package ai.stapi.graphoperations.objectGraphMapper.infrastructure.structureSchema;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import ai.stapi.graphoperations.objectGraphMapper.model.specific.InterfaceSpecificObjectGraphMapperFixStrategy;
import ai.stapi.graphoperations.objectGraphMapper.model.specific.exceptions.CannotFixInterfaceObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.InterfaceObjectGraphMapping;
import ai.stapi.schema.structureSchema.AbstractStructureType;
import ai.stapi.schema.structureSchema.BoxedPrimitiveStructureType;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaProvider;
import ai.stapi.schema.structureSchemaProvider.exception.CannotProvideStructureSchema;
import java.util.Map;

public class StructureSchemaInterfaceSpecificObjectGraphMapperFixStrategy
    implements InterfaceSpecificObjectGraphMapperFixStrategy {

  private final StructureSchemaProvider structureSchemaProvider;

  public StructureSchemaInterfaceSpecificObjectGraphMapperFixStrategy(
      StructureSchemaProvider structureSchemaProvider
  ) {
    this.structureSchemaProvider = structureSchemaProvider;
  }

  @Override
  public InterfaceObjectGraphMapping fix(
      InterfaceObjectGraphMapping interfaceObjectGraphMapping,
      Map.Entry<String, Object> fieldEntry,
      GraphDescriptionBuilder builder,
      String serializationType
  ) {
    if (!this.structureSchemaProvider.provideSchema().containsDefinition(serializationType)) {
      return interfaceObjectGraphMapping;
    }
    AbstractStructureType structure = null;
    try {
      structure = this.structureSchemaProvider.provideSpecific(serializationType);
    } catch (CannotProvideStructureSchema e) {
      throw new CannotFixInterfaceObjectGraphMapping(serializationType, e);
    }
    if (structure instanceof BoxedPrimitiveStructureType) {
      if (interfaceObjectGraphMapping.getGraphDescription() instanceof AbstractEdgeDescription) {
        GraphDescription graphDescription = new NullGraphDescription();
        if (
            !interfaceObjectGraphMapping
                .getGraphDescription()
                .getChildGraphDescriptions()
                .isEmpty()
        ) {
          graphDescription = interfaceObjectGraphMapping
              .getGraphDescription()
              .getChildGraphDescriptions()
              .get(0);
        }
        return new InterfaceObjectGraphMapping(
            interfaceObjectGraphMapping.getInterfaceUuid(),
            graphDescription
        );
      }

    }
    return interfaceObjectGraphMapping;
  }
}
