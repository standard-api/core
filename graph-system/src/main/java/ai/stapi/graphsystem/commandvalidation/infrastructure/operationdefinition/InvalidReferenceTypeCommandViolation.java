package ai.stapi.graphsystem.commandvalidation.infrastructure.operationdefinition;

import ai.stapi.graphsystem.commandvalidation.model.ActualValueCommandConstrainViolation;
import ai.stapi.graphsystem.commandvalidation.model.fieldPath.CommandViolationFieldPath;
import org.jetbrains.annotations.Nullable;

public class InvalidReferenceTypeCommandViolation extends ActualValueCommandConstrainViolation {

  @Nullable
  private Throwable cause;

  public InvalidReferenceTypeCommandViolation(
      String commandName,
      CommandViolationFieldPath fieldPath,
      Object actualValue
  ) {
    super(commandName, fieldPath, Level.ERROR, actualValue);
  }

  public InvalidReferenceTypeCommandViolation(
      String commandName,
      CommandViolationFieldPath fieldPath,
      Object actualValue,
      Throwable cause
  ) {
    this(commandName, fieldPath, actualValue);
    this.cause = cause;
  }

  @Override
  protected String getBecauseMessage() {
    return "Reference value was invalid. Object with single string field under key 'id' is required.";
  }

  @Nullable
  public Throwable getCause() {
    return cause;
  }
}
