package ai.stapi.graphoperations.ogmProviders.specific.dynamicObjectGraphMappingProvider.exception;

public class DynamicOgmProviderException extends RuntimeException {

  private DynamicOgmProviderException(String message) {
    super(message);
  }

  private DynamicOgmProviderException(String message, Throwable cause) {
    super(message, cause);
  }

  public static DynamicOgmProviderException becauseFieldNameIsBlank(String serializationType) {
    return new DynamicOgmProviderException(
        "Serialization type '"
            + serializationType
            + "' is primitive-type, and so fieldName parameter must be not blank."
    );
  }

  public static DynamicOgmProviderException becauseSerializationTypeIsPrimitive(
      String serializationType) {
    return new DynamicOgmProviderException(
        "Serialization type '"
            + serializationType
            + "' is primitive-type, use method for primitive OGM instead."
    );
  }

  public static DynamicOgmProviderException becauseSerializationTypeIsComplex(
      String serializationType) {
    return new DynamicOgmProviderException(
        "Serialization type '"
            + serializationType
            + "' is complex-type, use main method provideGraphMapping instead."
    );
  }

  public static DynamicOgmProviderException becauseThereWasNoStructureSchema(
      String serializationType,
      Throwable cause
  ) {
    return new DynamicOgmProviderException(
        String.format("Cannot provide Dynamic OGM for serializationType '%s'", serializationType),
        cause
    );
  }

  public static DynamicOgmProviderException becauseValueFieldForBoxedTypeIsMissing(
      String serializationType) {
    return new DynamicOgmProviderException(
        "Boxed type '" + serializationType + "' is missing 'value' field.");
  }

}
