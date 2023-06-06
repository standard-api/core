package ai.stapi.graphoperations.ogmProviders.specific.dynamicObjectGraphMappingProvider;

import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graphoperations.ogmProviders.specific.dynamicObjectGraphMappingProvider.exception.DynamicOgmProviderException;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ListAttributeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.SpecificObjectGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.InterfaceGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.LeafGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ListGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectFieldDefinitionBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ReferenceGraphMappingBuilder;
import ai.stapi.graphoperations.ogmProviders.specific.SpecificGraphMappingProvider;
import ai.stapi.schema.structureSchema.AbstractStructureType;
import ai.stapi.schema.structureSchema.BoxedPrimitiveStructureType;
import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.PrimitiveStructureType;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaProvider;
import ai.stapi.schema.structureSchemaProvider.exception.CannotProvideStructureSchema;
import java.util.HashMap;
import java.util.Map;

public class DynamicOgmProvider implements SpecificGraphMappingProvider {

  private final StructureSchemaProvider structureSchemaProvider;
  private final Map<String, ObjectGraphMapping> cachedComplexTypes = new HashMap<>();

  public DynamicOgmProvider(StructureSchemaProvider structureSchemaProvider) {
    this.structureSchemaProvider = structureSchemaProvider;
  }

  public ObjectGraphMapping provideOgmForPrimitive(
      String serializationType,
      FieldDefinition fieldDefinition
  ) {
    AbstractStructureType definition;
    try {
      definition = this.structureSchemaProvider.provideSpecific(serializationType);
    } catch (CannotProvideStructureSchema e) {
      throw DynamicOgmProviderException.becauseThereWasNoStructureSchema(serializationType, e);
    }
    if (definition instanceof PrimitiveStructureType) {
      if (fieldDefinition.isList()) {
        var leaf = this.resolveListPrimitiveType(serializationType);
        var listGraphMappingBuilder = new ListGraphMappingBuilder();
        listGraphMappingBuilder
            .setGraphDescription(new ListAttributeDescription(fieldDefinition.getName()))
            .setChildDefinition(leaf);

        return listGraphMappingBuilder.build();
      } else {
        return this.resolvePrimitiveType(
            fieldDefinition.getName(),
            serializationType
        ).build();
      }
    }
    if (definition instanceof BoxedPrimitiveStructureType boxedPrimitiveStructureType) {
      if (fieldDefinition.isList()) {
        var leaf = this.resolveListBoxedType(serializationType);
        var listGraphMappingBuilder = new ListGraphMappingBuilder();
        listGraphMappingBuilder
            .setGraphDescription(new ListAttributeDescription(fieldDefinition.getName()))
            .setChildDefinition(leaf);

        return listGraphMappingBuilder.build();
      } else {
        return this.resolveBoxedType(
            fieldDefinition.getName(),
            boxedPrimitiveStructureType.getOriginalDefinitionType()
        ).build();
      }
    }
    throw DynamicOgmProviderException.becauseSerializationTypeIsComplex(serializationType);
  }

  public ObjectGraphMapping provideGraphMapping(
      String serializationType
  ) {
    AbstractStructureType definition;
    try {
      definition = this.structureSchemaProvider.provideSpecific(serializationType);
    } catch (CannotProvideStructureSchema e) {
      throw DynamicOgmProviderException.becauseThereWasNoStructureSchema(serializationType, e);
    }
    return this.provideGraphMapping(serializationType, definition);
  }

  public ObjectGraphMapping provideGraphMapping(
      String serializationType,
      AbstractStructureType abstractStructureType
  ) {
    if (abstractStructureType instanceof PrimitiveStructureType) {
      throw DynamicOgmProviderException.becauseSerializationTypeIsPrimitive(serializationType);
    }
    if (abstractStructureType instanceof BoxedPrimitiveStructureType) {
      throw DynamicOgmProviderException.becauseSerializationTypeIsPrimitive(serializationType);
    }
    if (this.cachedComplexTypes.containsKey(serializationType)) {
      return this.cachedComplexTypes.get(serializationType);
    }
    var resolvedComplexType = this.resolveComplexType(
        (ComplexStructureType) abstractStructureType,
        new ObjectGraphMappingBuilder()
    ).build();
    this.cachedComplexTypes.put(serializationType, resolvedComplexType);
    return resolvedComplexType;
  }

  public boolean supports(String serializationType) {
    return this.structureSchemaProvider.provideSchema().containsDefinition(serializationType);
  }

  private ObjectGraphMappingBuilder resolveComplexType(
      ComplexStructureType complexStructureType,
      ObjectGraphMappingBuilder builder
  ) {
    builder.setGraphDescription(
        new GraphDescriptionBuilder().addNodeDescription(
            complexStructureType.getDefinitionType()
        )
    );
    complexStructureType.getAllFields().forEach(
        (key, value) -> {
          var fieldBuilder = new ObjectFieldDefinitionBuilder();
          if (key.equals("id")) {
            fieldBuilder.setChild(this.createIdentifyingField());
          } else {
            fieldBuilder.setChild(this.createChild(key, value));
          }
          builder.addField(key, fieldBuilder);
        }
    );
    return builder;
  }

  private SpecificObjectGraphMappingBuilder createChild(
      String fieldName,
      FieldDefinition fieldDefinition
  ) {
    if (fieldDefinition.isList()) {
      return this.resolveList(fieldName, fieldDefinition);
    } else {
      return resolveSingleChild(fieldName, fieldDefinition);
    }
  }

  private LeafGraphMappingBuilder createIdentifyingField() {

    return new LeafGraphMappingBuilder()
        .setGraphDescription(new UuidIdentityDescription());
  }

  private ListGraphMappingBuilder resolveList(
      String fieldName,
      FieldDefinition fieldDefinition
  ) {
    var builder = new ListGraphMappingBuilder();
    if (this.isFieldDefinitionRequiringAttribute(fieldDefinition)) {
      builder.setGraphDescription(
          new ListAttributeDescription(fieldName)
      );
    }
    builder.setChildDefinition(this.resolveChildInList(fieldDefinition));
    return builder;
  }

  private SpecificObjectGraphMappingBuilder resolveChildInList(
      FieldDefinition fieldDefinition
  ) {
    if (fieldDefinition.isUnionType() || fieldDefinition.isAnyType()) {
      return new InterfaceGraphMappingBuilder().setGraphDescription(
          new GraphDescriptionBuilder().addOutgoingEdge(fieldDefinition.getName())
      );
    } else {
      var fieldType = fieldDefinition.getTypes().get(0);
      if (fieldType.isPrimitiveType()) {
        return this.resolveListPrimitiveType(fieldType.getType());
      }
      if (fieldType.isBoxed()) {
        return this.resolveListBoxedType(fieldType.getType());
      }
      if (fieldType.isReference()) {
        var builder = new ObjectGraphMappingBuilder().setGraphDescription(
            new GraphDescriptionBuilder().addOutgoingEdge(fieldDefinition.getName())
        );
        builder.addField("id")
            .addLeafAsObjectFieldMapping()
            .setGraphDescription(
                new GraphDescriptionBuilder()
                    .addNodeDescription(fieldType.getType())
                    .addUuidDescription()
            );
        return builder;
      }
      return new ReferenceGraphMappingBuilder()
          .setReferencedSerializationType(fieldType.getType())
          .setGraphDescription(
              new GraphDescriptionBuilder().addOutgoingEdge(fieldDefinition.getName())
          );
    }
  }

  private SpecificObjectGraphMappingBuilder resolveSingleChild(
      String fieldName,
      FieldDefinition fieldDefinition
  ) {
    if (fieldDefinition.isUnionType() || fieldDefinition.isAnyType()) {
      return new InterfaceGraphMappingBuilder().setGraphDescription(
          new GraphDescriptionBuilder().addOutgoingEdge(fieldName)
      );
    } else {
      var fieldType = fieldDefinition.getTypes().get(0);
      if (fieldType.isPrimitiveType()) {
        return this.resolvePrimitiveType(
            fieldName,
            fieldType.getType()
        );
      }
      if (fieldType.isBoxed()) {
        return this.resolveBoxedType(
            fieldName,
            fieldType.getType()
        );
      }
      if (fieldType.isReference()) {
        var builder = new ObjectGraphMappingBuilder().setGraphDescription(
            new GraphDescriptionBuilder().addOutgoingEdge(fieldName)
        );
        builder.addField("id")
            .addLeafAsObjectFieldMapping()
            .setGraphDescription(
                new GraphDescriptionBuilder()
                    .addNodeDescription(fieldType.getType())
                    .addUuidDescription()
            );
        return builder;
      }

      return new ReferenceGraphMappingBuilder()
          .setReferencedSerializationType(fieldType.getType())
          .setGraphDescription(new GraphDescriptionBuilder().addOutgoingEdge(fieldName));
    }
  }

  private LeafGraphMappingBuilder resolvePrimitiveType(
      String fieldName,
      String dataType
  ) {
    return new LeafGraphMappingBuilder().setGraphDescription(
        new GraphDescriptionBuilder()
            .addAttributeByType(LeafAttribute.DATA_STRUCTURE_TYPE, fieldName)
            .addAttributeValueByType(dataType)
    );
  }

  private LeafGraphMappingBuilder resolveListPrimitiveType(
      String dataType
  ) {
    return new LeafGraphMappingBuilder().setGraphDescription(
        new GraphDescriptionBuilder().addAttributeValueByType(dataType)
    );
  }

  private SpecificObjectGraphMappingBuilder resolveListBoxedType(
      String originalType
  ) {
    var objectGraphMappingBuilder = new ObjectGraphMappingBuilder();
    objectGraphMappingBuilder
        .addField("value")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder().addAttributeValueByType(originalType));

    return objectGraphMappingBuilder;
  }

  private SpecificObjectGraphMappingBuilder resolveBoxedType(
      String fieldName,
      String originalType
  ) {
    var objectGraphMappingBuilder = new ObjectGraphMappingBuilder();
    objectGraphMappingBuilder
        .addField("value")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addAttributeByType(LeafAttribute.DATA_STRUCTURE_TYPE, fieldName)
                .addAttributeValueByType(originalType));

    return objectGraphMappingBuilder;
  }

  private boolean isFieldDefinitionRequiringEdge(FieldDefinition fieldDefinition) {
    if (fieldDefinition.isUnionType() || fieldDefinition.isAnyType()) {
      return true;
    }
    var fieldType = fieldDefinition.getTypes().get(0);
    if (fieldType.isPrimitiveType() || fieldType.isBoxed()) {
      return false;
    }
    return true;
  }

  private boolean isFieldDefinitionRequiringAttribute(FieldDefinition fieldDefinition) {
    return fieldDefinition.getTypes().stream().anyMatch(
        type -> type.isPrimitiveType() || type.isBoxed()
    );
  }
}
