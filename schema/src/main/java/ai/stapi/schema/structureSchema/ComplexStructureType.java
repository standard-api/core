package ai.stapi.schema.structureSchema;

import ai.stapi.schema.structureSchema.exception.FieldDoesNotExist;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ComplexStructureType extends AbstractStructureType {

  public static final String SERIALIZATION_TYPE = "ComplexStructureType";
  public static final String KIND = AbstractStructureType.COMPLEX_TYPE;

  private Map<String, FieldDefinition> fields;

  protected ComplexStructureType() {
    super();
  }

  protected ComplexStructureType(
      String serializationType,
      String kind,
      String definitionType,
      Map<String, FieldDefinition> fields,
      String description,
      String parent,
      boolean isAbstract
  ) {
    super(serializationType, kind, definitionType, description, isAbstract, parent);
    this.fields = new HashMap<>(fields);
  }

  public ComplexStructureType(
      String definitionType,
      Map<String, FieldDefinition> fields,
      String description,
      String parent,
      boolean isAbstract

  ) {
    super(
        SERIALIZATION_TYPE,
        AbstractStructureType.COMPLEX_TYPE,
        definitionType,
        description,
        isAbstract,
        parent
    );
    this.fields = new HashMap<>(fields);
  }

  public Map<String, FieldDefinition> getAllFields() {
    return fields;
  }

  public Map<String, FieldDefinition> getOwnFields() {
    return fields.entrySet().stream()
        .filter(entry -> entry.getValue().getParentDefinitionType().equals(this.definitionType))
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }

  public FieldDefinition getField(String fieldName) {
    if (!this.fields.containsKey(fieldName)) {
      throw new FieldDoesNotExist(
          String.format(
              "Field '%s' does not exist on '%s'.",
              fieldName,
              this.getDefinitionType()
          )
      );
    }
    return this.fields.get(fieldName);
  }

  public boolean hasField(String fieldName) {
    return this.fields.containsKey(fieldName);
  }

  public ComplexStructureType copyWithNewFields(Map<String, FieldDefinition> newFields) {
    return new ComplexStructureType(
        definitionType,
        newFields,
        description,
        parent,
        isAbstract
    );
  }
}
