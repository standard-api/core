package ai.stapi.schema.structureSchema;

import ai.stapi.schema.structureSchema.builder.ComplexStructureTypeBuilder;
import ai.stapi.schema.structureSchema.builder.StructureSchemaBuilder;
import ai.stapi.schema.structureSchema.exception.StructureSchemaCreationException;
import ai.stapi.schema.structureSchema.exception.StructureSchemaException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class StructureSchema {

  private Map<String, AbstractStructureType> definitions;

  public StructureSchema() {
    this.definitions = new HashMap<>();
  }

  public StructureSchema(Map<String, AbstractStructureType> abstractStructureType) {
    this.definitions = new HashMap<>(abstractStructureType);
    var unresolvableTypes = new StructureSchemaBuilder(
        this.definitions).getUnresolvableTypesWithFailingDependencyList();
    if (unresolvableTypes.size() > 0) {
      throw StructureSchemaCreationException.becauseItContainsUnresolvableTypes(unresolvableTypes);
    }
  }

  public StructureSchema(AbstractStructureType... types) {
    this(List.of(types));
  }

  public StructureSchema(List<AbstractStructureType> types) {
    this(typesToMap(types));
  }

  public static StructureSchema createChecked(List<AbstractStructureType> types) {
    var schema = new StructureSchema();
    schema.definitions = types.stream()
        .collect(Collectors.toMap(AbstractStructureType::getDefinitionType, Function.identity()));
    return schema;
  }

  @NotNull
  private static HashMap<String, AbstractStructureType> typesToMap(
      List<AbstractStructureType> types) {
    var definitions = new HashMap<String, AbstractStructureType>();
    types.forEach(type -> definitions.put(type.getDefinitionType(), type));
    return definitions;
  }

  public Map<String, AbstractStructureType> getStructureTypes() {
    return definitions;
  }

  public AbstractStructureType getDefinition(String serializationType) {
    return this.definitions.get(serializationType);
  }

  public boolean containsDefinition(String serializationType) {
    return this.definitions.containsKey(serializationType);
  }

  public StructureSchema merge(StructureSchema other) {
    var schemaBuilder = new StructureSchemaBuilder(this);
    other.definitions.forEach(
        (key, structureType) -> {
          if (this.definitions.containsKey(key)
              && !this.definitions.get(key).equals(structureType)) {
            var localStructure = this.definitions.get(key);
            if (
                localStructure instanceof ComplexStructureType complexStructureType
                    && !(localStructure instanceof BoxedPrimitiveStructureType)) {
              var otherStructure = (ComplexStructureType) structureType;
              var otherFields = otherStructure.getAllFields();
              var localFields = complexStructureType.getAllFields();
              var combinedFields = new HashMap<String, FieldDefinition>();
              combinedFields.putAll(localFields);
              combinedFields.putAll(otherFields);
              var newStructure = new ComplexStructureTypeBuilder().copyToBuilder(localStructure);
              if (!otherStructure.getParent().isBlank()) {
                newStructure.setParent(otherStructure.getParent());
              }
              newStructure.setParent(otherStructure.getParent());
              combinedFields.forEach(
                  (fieldName, field) -> {
                    newStructure.addField(fieldName)
                        .setMin(field.getMin())
                        .setMax(field.getMax())
                        .setName(field.getName())
                        .setDescription(field.getDescription())
                        .setTypes(field.getTypes())
                        .setParentDefinitionType(field.getParentDefinitionType());
                  }
              );
              this.definitions.put(key, newStructure.build(schemaBuilder));
            } else {
              this.definitions.put(key, structureType);
            }
          } else {
            this.definitions.put(key, structureType);
          }
        }
    );
    this.resolveParentFields();
    return this;
  }

  private void resolveParentFields() {
    var updatedDefinitions = new ArrayList<ComplexStructureType>();
    definitions.forEach((key, definition) -> {
      if (definition instanceof ComplexStructureType complexStructureType) {
        var parentFields = this.collectParentFields(complexStructureType);
        var definitionFields = complexStructureType.getAllFields();
        parentFields.putAll(definitionFields);
        if (complexStructureType instanceof BoxedPrimitiveStructureType boxed) {
          updatedDefinitions.add(boxed.copyWithNewFields(parentFields));
        } else if (complexStructureType instanceof ResourceStructureType resource) {
          updatedDefinitions.add(resource.copyWithNewFields(parentFields));
        } else {
          updatedDefinitions.add(complexStructureType.copyWithNewFields(parentFields));
        }
      }
    });
    updatedDefinitions.forEach(
        updatedDefinition -> this.definitions.put(updatedDefinition.getDefinitionType(),
            updatedDefinition));
  }

  private Map<String, FieldDefinition> collectParentFields(
      ComplexStructureType complexStructureType) {
    var parent = complexStructureType.getParent();
    if (parent.isBlank()) {
      return new HashMap<>();
    }
    if (!this.definitions.containsKey(parent)) {
      throw StructureSchemaException.becauseParentDefinitionIsMissing(
          complexStructureType.getDefinitionType(), parent);
    }
    var parentDefinition = this.definitions.get(parent);
    if (parentDefinition instanceof ComplexStructureType complexParent) {
      var map = this.collectParentFields(complexParent);
      map.putAll(complexParent.getAllFields());
      return map;
    }
    return new HashMap<>();
  }

  public List<AbstractStructureType> getChildDefinitions(String parentSerializationType) {
    return this.definitions.values().stream()
        .filter(abstractStructureType -> abstractStructureType.getParent() != null)
        .filter(abstractStructureType -> abstractStructureType.getParent()
            .equals(parentSerializationType))
        .toList();
  }

  public boolean has(String serializationType) {
    return this.definitions.containsKey(serializationType);
  }
}
