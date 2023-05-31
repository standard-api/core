package ai.stapi.schema.structureSchemaProvider;

import ai.stapi.schema.structureSchema.AbstractStructureType;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.ResourceStructureType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public interface StructureSchemaFinder {

  boolean isList(String serializationType, String fieldName);

  boolean containsType(String rootType, String typeToBeFound);

  AbstractStructureType getStructureType(String serializationType);

  Map<String, FieldDefinition> getFieldDefinitionsFor(
      String serializationType,
      List<String> fieldNames
  );

  FieldDefinition getFieldDefinitionFor(
      String serializationType,
      String fieldName
  );

  Map<String, FieldDefinition> getAllFieldDefinitionsFor(
      String serializationType
  );


  @NotNull
  HashMap<String, FieldDefinition> getFieldDefinitionHashMap(
      Map<?, ?> attributes,
      String serializationType
  );

  boolean isResource(String serializationType);

  boolean structureExists(String serializationType);

  AbstractStructureType getSchemaStructure(String serializationType);

  FieldDefinition getFieldDefinitionOrFallback(String serializationType, String fieldName);

  List<ResourceStructureType> getAllResources();

  boolean inherits(
      String serializationType,
      String inheritedSerializationType
  );

  boolean isEqualOrInherits(
      String serializationType,
      String inheritedSerializationType
  );
}
