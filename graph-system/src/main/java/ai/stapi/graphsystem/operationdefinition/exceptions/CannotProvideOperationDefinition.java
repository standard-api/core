package ai.stapi.graphsystem.operationdefinition.exceptions;

public class CannotProvideOperationDefinition extends RuntimeException {

  public CannotProvideOperationDefinition(String operationDefinitionName) {
    super(
        String.format(
            "Cannot Provide operation definition for: '%s', because it does not exists.",
            operationDefinitionName
        )
    );
  }
}
