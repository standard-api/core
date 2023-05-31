package ai.stapi.schema.structureSchema.builder;

import ai.stapi.schema.structureSchema.FieldType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractComplexStructureTypeBuilder extends AbstractStructureTypeBuilder {

  protected final Map<String, FieldDefinitionBuilder> fields = new HashMap<>();

  public Map<String, FieldDefinitionBuilder> getAllFields(
      StructureSchemaBuilder structureSchemaBuilder
  ) {
    var allFields = new HashMap<>(this.fields);
    if (this.getParent() != null && !this.getParent().isBlank()) {
      var parentBuilder = structureSchemaBuilder.getStructureTypeBuilder(this.getParent());
      if (parentBuilder != null) {
        var parentFields = parentBuilder.getAllFields(structureSchemaBuilder);
        parentFields.forEach((fieldName, fieldDefinitionBuilder) -> {
          if (!allFields.containsKey(fieldName)) {
            allFields.put(
                fieldName,
                fieldDefinitionBuilder
            );
          }
        });

      }
    }
    return allFields;
  }

  public FieldDefinitionBuilder addField(String fieldName) {
    var fieldBuilder = new FieldDefinitionBuilder()
        .setName(fieldName)
        .setParentDefinitionType(this.serializationType);
    fields.put(
        fieldName,
        fieldBuilder
    );
    return fieldBuilder;
  }

  public List<FieldType> getAllFieldTypes() {
    return this.fields.values().stream()
        .map(FieldDefinitionBuilder::getTypes)
        .flatMap(List::stream)
        .distinct()
        .toList();
  }

}
