package ai.stapi.graphsystem.operationdefinition.exceptions;

public class CannotMapOperationDefinitionToStructureType extends RuntimeException {

  public CannotMapOperationDefinitionToStructureType(
      String operationName,
      String parameterName,
      String parameterType,
      Throwable cause
  ) {
    super(
        String.format(
            "Cannot map Operation Definition '%s' to Structure Type, " +
                "because it had parameter '%s' of type '%s' which does not have Structure Schema.",
            operationName,
            parameterName,
            parameterType
        ),
        cause
    );
  }
}
