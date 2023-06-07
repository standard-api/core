package ai.stapi.graphsystem.operationdefinition.exceptions;


public class CannotMergeOperationDefinition extends RuntimeException {

  private CannotMergeOperationDefinition(String becauseMessage) {
    super("Cannot merge Operation Definition, because " + becauseMessage);
  }

  public static CannotMergeOperationDefinition becauseSomeFieldIsNotSame(
      String fieldName,
      Object originalValue,
      Object otherValue
  ) {
    return new CannotMergeOperationDefinition(
        String.format(
            "field '%s' has different value, but they should be same.%nOriginal: '%s'%nOther: '%s'",
            fieldName,
            originalValue,
            otherValue
        )
    );
  }

  public static CannotMergeOperationDefinition becauseSomeParameterHasSomeFieldWhichIsNotSame(
      String parameterName,
      String fieldName,
      Object originalValue,
      Object otherValue
  ) {
    return new CannotMergeOperationDefinition(
        String.format(
            "parameter '%s' has field '%s' with different values, "
                + "but they should be same.%nOriginal: '%s'%nOther: '%s'",
            parameterName,
            fieldName,
            originalValue,
            otherValue
        )
    );
  }
}
