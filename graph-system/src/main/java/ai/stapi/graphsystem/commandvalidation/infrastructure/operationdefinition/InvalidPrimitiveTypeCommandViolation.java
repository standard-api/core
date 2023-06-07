package ai.stapi.graphsystem.commandvalidation.infrastructure.operationdefinition;

import ai.stapi.graphsystem.commandvalidation.model.ActualValueCommandConstrainViolation;
import ai.stapi.graphsystem.commandvalidation.model.fieldPath.CommandViolationFieldPath;

public class InvalidPrimitiveTypeCommandViolation extends ActualValueCommandConstrainViolation {

  private final String dataType;

  public InvalidPrimitiveTypeCommandViolation(
      String commandName,
      CommandViolationFieldPath fieldPath,
      String dataType,
      Object actualValue
  ) {
    super(commandName, fieldPath, Level.ERROR, actualValue);
    this.dataType = dataType;
  }

  @Override
  protected String getBecauseMessage() {
    return String.format(
        "Primitive value was of invalid data type. Required data type: %s.",
        this.dataType
    );
  }

  public String getDataType() {
    return dataType;
  }
}
