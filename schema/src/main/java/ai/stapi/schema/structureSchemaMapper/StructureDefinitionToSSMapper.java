package ai.stapi.schema.structureSchemaMapper;

import ai.stapi.schema.structureSchema.AbstractStructureType;
import ai.stapi.schema.structureSchema.FieldType;
import ai.stapi.schema.structureSchema.PrimitiveStructureType;
import ai.stapi.schema.structureSchema.StructureSchema;
import ai.stapi.schema.structureSchema.builder.AbstractStructureTypeBuilder;
import ai.stapi.schema.structureSchema.builder.ComplexStructureTypeBuilder;
import ai.stapi.schema.structureSchema.builder.PrimitiveStructureTypeBuilder;
import ai.stapi.schema.structureSchema.builder.StructureSchemaBuilder;
import ai.stapi.schema.structureSchemaMapper.exception.UnresolvableTypeException;
import ai.stapi.schema.structuredefinition.ElementDefinition;
import ai.stapi.schema.structuredefinition.ElementDefinitionType;
import ai.stapi.schema.structuredefinition.StructureDefinitionData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

public class StructureDefinitionToSSMapper {

  public MappingOutcome map(StructureDefinitionData structureDefinitions) {
    return this.map(List.of(structureDefinitions), new StructureSchema());
  }

  public MappingOutcome map(StructureDefinitionData structureDefinitions, StructureSchema context) {
    return this.map(List.of(structureDefinitions), context);
  }

  public MappingOutcome map(List<StructureDefinitionData> structureDefinitions) {
    return this.map(structureDefinitions, new StructureSchema());
  }

  public MappingOutcome map(
      List<StructureDefinitionData> structureDefinitions,
      StructureSchema context
  ) {
    if (structureDefinitions.isEmpty()) {
      return new MappingOutcome(context, new ArrayList<>(), new ArrayList<>());
    }
    var schemaBuilder = new StructureSchemaBuilder(context);
    structureDefinitions.forEach(
        definition -> {
          if (definition.getKind() == null) {
            throw new UnresolvableTypeException("Kind is null for " + definition.getId());
          }
          if (definition.getKind().equals(PrimitiveStructureType.KIND)) {
            schemaBuilder.addStructureTypeBuilder(
                this.createPrimitiveTypeBuilder(definition)
            );
          } else {
            schemaBuilder.addStructureTypeBuilders(
                this.createComplexTypeBuilders(definition)
            );
          }
        });

    var unresolvableSerializationTypesWithCause =
        schemaBuilder.getUnresolvableTypesWithFailingDependencyList();

    var unresolvableTypes = unresolvableSerializationTypesWithCause.stream()
        .map(unresolvableSerializationType -> {
          var structureDefinitionDTO = structureDefinitions.stream()
              .filter(
                  definition -> definition.getId()
                      .equals(unresolvableSerializationType.serializationType())
              )
              .reduce(StructureDefinitionDTOMerger::merge)
              .orElse(null);

          return new UnresolvableType(
              structureDefinitionDTO, unresolvableSerializationType.missingDependencies()
          );
        })
        .filter(unresolvableType -> unresolvableType.structureDefinitionData() != null)
        .toList();
    var unresolvableSerializationTypes = unresolvableTypes.stream().map(
        UnresolvableType::structureDefinitionData
    ).map(StructureDefinitionData::getId).toList();

    var successfullyResolvedTypes = structureDefinitions.stream()
        .map(StructureDefinitionData::getId)
        .filter(id -> !unresolvableSerializationTypes.contains(id))
        .distinct()
        .toList();

    StructureSchema builtSchema = schemaBuilder.build();

    return new MappingOutcome(
        builtSchema,
        unresolvableTypes,
        successfullyResolvedTypes
    );

  }

  public String createAnonymousComplexTypeNameFromElementPath(String elementDefinitionPath) {
    var pathParts = elementDefinitionPath.split("\\.");
    return Arrays.stream(pathParts)
        .map(StringUtils::capitalize)
        .collect(Collectors.joining());
  }

  private AbstractStructureTypeBuilder createPrimitiveTypeBuilder(
      StructureDefinitionData definitionDTO
  ) {
    var builder = new PrimitiveStructureTypeBuilder();
    builder
        .setSerializationType(definitionDTO.getId())
        .setDescription(definitionDTO.getDescription())
        .setIsAbstract(definitionDTO.getIsAbstract());
    if (definitionDTO.getBaseDefinitionReference() != null) {
      builder.setParent(definitionDTO.getBaseDefinitionReference().toString());
    }
    return builder;
  }

  private List<AbstractStructureTypeBuilder> createComplexTypeBuilders(
      StructureDefinitionData structureDefinitionData
  ) {
    var builder = new ComplexStructureTypeBuilder();
    builder
        .setSerializationType(structureDefinitionData.getId())
        .setDescription(structureDefinitionData.getDescription())
        .setIsAbstract(structureDefinitionData.getIsAbstract())
        .setKind(structureDefinitionData.getKind());
    if (structureDefinitionData.getBaseDefinitionReference() != null) {
      builder.setParent(structureDefinitionData.getBaseDefinitionReference().toString());
    }

    var resultBuilders = new ArrayList<AbstractStructureTypeBuilder>();
    resultBuilders.add(builder);

    var differential = structureDefinitionData.getDifferential();
    if (differential == null) {
      return resultBuilders;
    }

    differential.getElement().stream()
        .filter(definition -> definition.getPath().split("\\.").length == 2)
        .forEach(elementDefinition -> {
          var fieldName = elementDefinition.getPath().split("\\.")[1].replace("[x]", "");
          var elementTypes = this.resolveElementTypes(elementDefinition);
          var fieldBuilder = builder.addField(fieldName);
          fieldBuilder.setMin(elementDefinition.getMin())
              .setMax(elementDefinition.getMax())
              .setDescription(elementDefinition.getDefinition());

          if (elementDefinition.getContentReference() != null) {
            var referencedType = this.createContentReferencedType(
                structureDefinitionData,
                elementDefinition.getContentReference()
            );
            if (referencedType != null) {
              fieldBuilder.addType(referencedType);
            } else {
              //TODO: Add to failed types or something?
            }
          }

          elementTypes.forEach(fieldBuilder::addType);
        });

    var anonymousBuilders = new HashMap<String, ComplexStructureTypeBuilder>();
    differential.getElement().forEach(elementDefinition -> {
      var splitPath = elementDefinition.getPath().split("\\.");
      if (splitPath.length < 3) {
        return;
      }
      var fieldName = splitPath[splitPath.length - 1].replace("[x]", "");
      var anonymousSplitPath = Arrays.copyOfRange(splitPath, 0, splitPath.length - 1);
      var anonymousPath = String.join(".", anonymousSplitPath);
      var anonymousObjectName = this.createAnonymousComplexTypeNameFromElementPath(anonymousPath);
      var anonymousBuilder = anonymousBuilders.computeIfAbsent(
          anonymousObjectName,
          key -> this.createAnonymousBuilder(structureDefinitionData, anonymousObjectName)
      );
      var maybeParent = differential.getElement().stream()
          .filter(definition -> definition.getPath().equals(anonymousPath))
          .findFirst();

      maybeParent.ifPresent(
          parent -> {
            var type = parent.getType();
            if (type.isEmpty()) {
              return;
            }
            anonymousBuilder.setParent(type.get(0).getCode());
          }
      );
      var fieldTypes = this.resolveElementTypes(elementDefinition);
      var fieldBuilder = anonymousBuilder.addField(fieldName);
      fieldBuilder.setMin(elementDefinition.getMin())
          .setMax(elementDefinition.getMax())
          .setDescription(elementDefinition.getDefinition());

      if (elementDefinition.getContentReference() != null) {
        var referencedType = this.createContentReferencedType(
            structureDefinitionData,
            elementDefinition.getContentReference()
        );
        if (referencedType != null) {
          fieldBuilder.addType(referencedType);
        } else {
          //TODO: Add to failed types or something?
        }
      }

      fieldTypes.forEach(fieldBuilder::addType);
    });

    resultBuilders.addAll(anonymousBuilders.values());
    return resultBuilders;
  }

  private ComplexStructureTypeBuilder createAnonymousBuilder(
      StructureDefinitionData containedInDefinition,
      String anonymousObjectName
  ) {
    return new ComplexStructureTypeBuilder()
        .setSerializationType(anonymousObjectName)
        .setKind(AbstractStructureType.COMPLEX_TYPE)
        .setIsAbstract(false)
        .setDescription("Type for anonymous field contained in " + containedInDefinition.getId())
        .setContainedInNonAnonymousType(containedInDefinition.getId());
  }

  private List<FieldType> resolveElementTypes(
      ElementDefinition elementDefinition
  ) throws UnresolvableTypeException {
    var fixedTypes = new ArrayList<FieldType>();
    if (elementDefinition.getType() == null) {
      return fixedTypes;
    }
    elementDefinition.getType().forEach(type -> {
      fixedTypes.addAll(this.createFieldType(elementDefinition, type));
    });
    return fixedTypes;
  }


  private List<FieldType> createFieldType(
      ElementDefinition elementDefinition,
      ElementDefinitionType type
  ) {
    if (this.isAnonymousElement(type.getCode())) {
      return List.of(
          FieldType.asAnonymousType(
              this.createAnonymousComplexTypeNameFromElementPath(elementDefinition.getPath()),
              type.getCode()
          )
      );
    } else if (isPrimitiveType(type.getCode()) && isUnionType(elementDefinition)) {
      return List.of(FieldType.asBoxedType(type.getCode()));
    } else if (isReference(type.getCode())) {
      if (type.getTargetProfile().isEmpty()) {
        return List.of(FieldType.asReferenceType("Reference"));
      }
      return type.getTargetProfile().stream()
          .map(targetProfile -> {
            var splitTargetProfile = targetProfile.split("/");
            var targetType = splitTargetProfile[splitTargetProfile.length - 1];
            return FieldType.asReferenceType(targetType);
          }).toList();
    } else {
      return List.of(FieldType.asPlainType(type.getCode()));
    }
  }

  private boolean isAnonymousElement(String type) {
    return type.equals("BackboneElement") || type.equals("Element");
  }

  private boolean isReference(String type) {
    return type.equals("Reference");
  }

  private boolean isPrimitiveType(String type) {
    return Character.isLowerCase(type.charAt(0));
  }

  private boolean isUnionType(ElementDefinition elementDefinition) {
    return elementDefinition.getType().size() > 1;
  }

  @Nullable
  private FieldType createContentReferencedType(
      StructureDefinitionData containedInDefinition,
      String referencedTypePath
  ) {
    var trimmedPath = referencedTypePath.replace("#", "");

    var referencedElementDefinition = containedInDefinition.getDifferential()
        .getElement()
        .stream()
        .filter(definition -> definition.getPath().equals(trimmedPath))
        .findFirst();

    if (referencedElementDefinition.isPresent()) {
      var anonymousTypeName = this.createAnonymousComplexTypeNameFromElementPath(
          trimmedPath
      );
      return FieldType.asContentReferenced(
          anonymousTypeName,
          referencedElementDefinition.get().getType().get(0).getCode()
      );
    }
    return null;
  }
}
