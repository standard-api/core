package ai.stapi.schema.structureSchema.exception;


public class StructureNotFoundException extends RuntimeException {

  private StructureNotFoundException(String message, String serializationType) {
    super(String.format("Structure with serializationType '%s' not found, %s", serializationType,
        message));
  }

  public static StructureNotFoundException becauseDoesNotExist(
      String serializationType
  ) {
    return new StructureNotFoundException(
        String.format(
            "because it does not exist"
        ),
        serializationType
    );
  }

  public static StructureNotFoundException becauseIsOfPrimitiveType(
      String serializationType
  ) {
    return new StructureNotFoundException(
        String.format(
            "because it is of primitive type"
        ),
        serializationType
    );
  }

  public static StructureNotFoundException becauseSerializationTypeIsOfUnknownType(
      String serializationType
  ) {
    return new StructureNotFoundException(
        String.format(
            "because it is of unknown type"
        ),
        serializationType
    );
  }

}
