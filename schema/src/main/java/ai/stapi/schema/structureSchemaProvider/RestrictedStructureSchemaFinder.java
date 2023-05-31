package ai.stapi.schema.structureSchemaProvider;

import ai.stapi.schema.structureSchema.AbstractStructureType;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.ResourceStructureType;
import ai.stapi.schema.structureSchema.exception.FieldsNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class RestrictedStructureSchemaFinder implements StructureSchemaFinder {

  @Override
  public boolean isList(String serializationType, String fieldName) throws FieldsNotFoundException {
    return this.getFieldDefinitionFor(serializationType, fieldName).isList();
  }

  @Override
  public boolean containsType(String rootType, String typeToBeFound) {
    return false;
  }

  @Override
  public AbstractStructureType getStructureType(String serializationType) {
    throw FieldsNotFoundException.becauseFinderIsRestricted(serializationType);
  }

  @Override
  public Map<String, FieldDefinition> getFieldDefinitionsFor(
      String serializationType,
      List<String> fieldNames
  ) throws FieldsNotFoundException {
    throw FieldsNotFoundException.becauseFinderIsRestricted(serializationType, fieldNames);
  }

  @Override
  public FieldDefinition getFieldDefinitionFor(
      String serializationType,
      String fieldName
  ) throws FieldsNotFoundException {
    return this.getFieldDefinitionsFor(serializationType, List.of(fieldName)).get(fieldName);
  }

  @Override
  public Map<String, FieldDefinition> getAllFieldDefinitionsFor(
      String serializationType
  ) throws FieldsNotFoundException {
    throw FieldsNotFoundException.becauseFinderIsRestricted(serializationType);
  }

  @NotNull
  @Override
  public HashMap<String, FieldDefinition> getFieldDefinitionHashMap(Map<?, ?> attributes,
      String serializationType) {
    throw FieldsNotFoundException.becauseFinderIsRestricted(serializationType);
  }

  @Override
  public boolean isResource(String serializationType) {
    throw FieldsNotFoundException.becauseFinderIsRestricted(serializationType);
  }

  @Override
  public boolean structureExists(String serializationType) {
    return false;
  }

  @Override
  public AbstractStructureType getSchemaStructure(String serializationType) {
    throw FieldsNotFoundException.becauseFinderIsRestricted(serializationType);
  }

  @Override
  public FieldDefinition getFieldDefinitionOrFallback(String serializationType, String fieldName) {
    throw FieldsNotFoundException.becauseFinderIsRestricted(serializationType);
  }

  @Override
  public List<ResourceStructureType> getAllResources() {
    return List.of();
  }

  @Override
  public boolean inherits(String serializationType, String inheritedSerializationType) {
    return false;
  }

  @Override
  public boolean isEqualOrInherits(String serializationType, String inheritedSerializationType) {
    return false;
  }
}
