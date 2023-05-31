package ai.stapi.schema.structureSchemaMapper.exception;

public class StructureDefinitionToSSMapperException extends RuntimeException {

  private final String failedStructureSerializationType;

  private StructureDefinitionToSSMapperException(String message,
      String failedStructureSerializationType) {
    super(message);
    this.failedStructureSerializationType = failedStructureSerializationType;
  }

  public static StructureDefinitionToSSMapperException becauseStructureDefinitionWasNotFound(
      String structureDefinitionId,
      String elementPath
  ) {
    return new StructureDefinitionToSSMapperException(
        "StructureDefinition of type '"
            + structureDefinitionId
            + "' on field '"
            + elementPath + "' was not found.",
        structureDefinitionId
    );
  }

  public static StructureDefinitionToSSMapperException becauseStructureDefinitionIsMissingKind(
      String structureDefinitionId
  ) {
    return new StructureDefinitionToSSMapperException(
        "StructureDefinition of type '" + structureDefinitionId + "' has field 'kind' not defined.",
        structureDefinitionId
    );
  }

  public String getFailedStructureSerializationType() {
    return failedStructureSerializationType;
  }
}
