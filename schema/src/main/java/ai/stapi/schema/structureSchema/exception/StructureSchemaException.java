package ai.stapi.schema.structureSchema.exception;


public class StructureSchemaException extends RuntimeException {

  private final String parentDefinitionType;

  private StructureSchemaException(String message, String parentDefinitionType) {
    super(message);
    this.parentDefinitionType = parentDefinitionType;
  }

  public static StructureSchemaException becauseCouldNotConstructDefinition(
      String parentDefinitionType,
      String fieldName,
      String fieldType
  ) {
    return new StructureSchemaException(
        "GraphDefinition does not contain definition for type '" + fieldType + "'" +
            System.lineSeparator() +
            "Parent Object: " + parentDefinitionType +
            System.lineSeparator() +
            "Field: " + fieldName,
        parentDefinitionType
    );
  }

  public static StructureSchemaException becauseParentDefinitionIsMissing(
      String definitionType,
      String parentDefinitionType
  ) {
    return new StructureSchemaException(
        "StructureSchema does not contain definition of parent for type '" + definitionType + "'." +
            System.lineSeparator() +
            "Required parent: " + parentDefinitionType,
        parentDefinitionType
    );
  }

  public String getParentDefinitionType() {
    return parentDefinitionType;
  }
}
