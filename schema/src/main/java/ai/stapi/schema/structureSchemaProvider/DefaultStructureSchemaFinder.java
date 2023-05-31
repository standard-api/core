package ai.stapi.schema.structureSchemaProvider;

import ai.stapi.schema.structureSchema.AbstractStructureType;
import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.FieldType;
import ai.stapi.schema.structureSchema.PrimitiveStructureType;
import ai.stapi.schema.structureSchema.ResourceStructureType;
import ai.stapi.schema.structureSchema.exception.FieldsNotFoundException;
import ai.stapi.schema.structureSchemaProvider.exception.CannotProvideStructureSchema;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class DefaultStructureSchemaFinder implements StructureSchemaFinder {

  public static final String UNKNOWN_FIELD_TYPE = "unknown";
  private final StructureSchemaProvider structureSchemaProvider;

  public DefaultStructureSchemaFinder(
      StructureSchemaProvider structureSchemaProvider
  ) {
    this.structureSchemaProvider = structureSchemaProvider;
  }

  @Override
  public boolean isList(String serializationType, String fieldName) throws FieldsNotFoundException {
    return this.getFieldDefinitionFor(serializationType, fieldName).isList();
  }

  @Override
  public boolean containsType(String rootType, String typeToBeFound) {
    var rootStructure = this.getStructureType(rootType);
    if (!(rootStructure instanceof ComplexStructureType complexRootStructure)) {
      return false;
    }
    return complexRootStructure.getOwnFields().values().stream().anyMatch(
        fieldDefinition -> fieldDefinition.getTypes().stream().anyMatch(
            type -> {
              if (type.getType().equals(typeToBeFound)) {
                return true;
              }
              return this.containsType(type.getType(), typeToBeFound);
            }
        )
    );
  }

  @Override
  public AbstractStructureType getStructureType(String serializationType) {
    var schema = this.structureSchemaProvider.provideSchema();
    if (!schema.has(serializationType)) {
      var unresolvable = this.structureSchemaProvider.provideUnresolvableTypes();
      var foundUnresolvable = unresolvable.stream()
          .filter(type -> type.structureDefinitionData().getId().equals(serializationType))
          .toList();

      if (foundUnresolvable.isEmpty()) {
        throw FieldsNotFoundException.becauseSerializationTypeDoesNotExistInSchema(
            serializationType
        );
      }
      throw FieldsNotFoundException.becauseSerializationTypeDoesNotExistInSchema(
          serializationType,
          foundUnresolvable.get(0)
      );
    }
    return schema.getDefinition(serializationType);
  }

  @Override
  public Map<String, FieldDefinition> getFieldDefinitionsFor(
      String serializationType,
      List<String> fieldNames
  ) throws FieldsNotFoundException {
    if (fieldNames.isEmpty()) {
      return new HashMap<>();
    }
    var structureType = this.getStructureType(serializationType);
    if (structureType instanceof PrimitiveStructureType) {
      throw FieldsNotFoundException.becauseSerializationTypeIsPrimitive(
          serializationType,
          fieldNames
      );
    }

    if (structureType instanceof ComplexStructureType complexStructureType) {
      var missingFields = fieldNames.stream().
          filter(fieldName -> !complexStructureType.getAllFields().containsKey(fieldName))
          .toList();

      if (!missingFields.isEmpty()) {
        throw FieldsNotFoundException.becauseSomeFieldsAreMissing(serializationType, missingFields);
      }

      return complexStructureType.getAllFields().entrySet().stream()
          .filter(entry -> fieldNames.contains(entry.getKey()))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    throw FieldsNotFoundException.becauseSerializationTypeIsOfUnknownType(
        serializationType,
        fieldNames
    );
  }

  @Override
  public FieldDefinition getFieldDefinitionFor(
      String serializationType,
      String fieldName
  ) throws FieldsNotFoundException {
    return this.getFieldDefinitionsFor(
        serializationType,
        List.of(fieldName)
    ).get(fieldName);
  }

  @Override
  public Map<String, FieldDefinition> getAllFieldDefinitionsFor(
      String serializationType
  ) throws FieldsNotFoundException {
    var schema = this.structureSchemaProvider.provideSchema();
    if (!schema.has(serializationType)) {
      throw FieldsNotFoundException.becauseSerializationTypeIsPrimitive(serializationType);
    }
    var structureType = schema.getDefinition(serializationType);

    if (structureType instanceof PrimitiveStructureType) {
      throw FieldsNotFoundException.becauseSerializationTypeIsPrimitive(serializationType);
    }

    if (structureType instanceof ComplexStructureType complexStructureType) {
      return complexStructureType.getAllFields();
    }

    throw FieldsNotFoundException.becauseSerializationTypeIsOfUnknownType(serializationType);
  }


  @NotNull
  @Override
  public HashMap<String, FieldDefinition> getFieldDefinitionHashMap(
      Map<?, ?> attributes,
      String structureId
  ) {
    var fieldDefinitions = new HashMap<String, FieldDefinition>();
    attributes.keySet().stream()
        .filter(String.class::isInstance)
        .map(String.class::cast)
        .forEach(fieldName -> {
          FieldDefinition fieldDefinitionFor = getFieldDefinitionOrFallback(
              structureId,
              fieldName
          );

          fieldDefinitions.put(
              fieldName,
              fieldDefinitionFor
          );
        });
    return fieldDefinitions;
  }

  @Override
  public boolean isResource(String serializationType) {
    var schema = this.structureSchemaProvider.provideSchema();
    if (!schema.containsDefinition(serializationType)) {
      return false;
    }
    return schema.getDefinition(serializationType).getKind().equals(ResourceStructureType.KIND);
  }

  @Override
  public boolean structureExists(String serializationType) {
    return this.structureSchemaProvider.containsSchema(serializationType);
  }

  @Override
  public AbstractStructureType getSchemaStructure(String serializationType) {
    try {
      return this.structureSchemaProvider.provideSpecific(serializationType);
    } catch (CannotProvideStructureSchema e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public FieldDefinition getFieldDefinitionOrFallback(String serializationType, String fieldName) {
    FieldDefinition fieldDefinitionFor;
    try {
      fieldDefinitionFor = this.getFieldDefinitionFor(
          serializationType,
          fieldName
      );
    } catch (FieldsNotFoundException e) {
      fieldDefinitionFor = new FieldDefinition(
          fieldName,
          0,
          "*",
          "",
          List.of(
              new FieldType(
                  UNKNOWN_FIELD_TYPE,
                  UNKNOWN_FIELD_TYPE
              )
          ),
          serializationType
      );
    }
    return fieldDefinitionFor;
  }

  @Override
  public List<ResourceStructureType> getAllResources() {
    return this.structureSchemaProvider.provideSchema()
        .getStructureTypes()
        .values()
        .stream()
        .filter(ResourceStructureType.class::isInstance)
        .map(ResourceStructureType.class::cast)
        .toList();
  }

  @Override
  public boolean inherits(String serializationType, String inheritedSerializationType) {
    var inheriting = this.getStructureType(serializationType);
    var parentType = inheriting.getParent();
    if (parentType == null || parentType.isBlank()) {
      return false;
    }
    if (parentType.equals(inheritedSerializationType)) {
      return true;
    }
    return this.inherits(parentType, inheritedSerializationType);
  }

  @Override
  public boolean isEqualOrInherits(String serializationType, String inheritedSerializationType) {
    if (serializationType.equals(inheritedSerializationType)) {
      return true;
    }
    return this.inherits(serializationType, inheritedSerializationType);
  }
}
