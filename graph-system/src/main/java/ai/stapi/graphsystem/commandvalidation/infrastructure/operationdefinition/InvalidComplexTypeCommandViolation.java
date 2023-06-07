package ai.stapi.graphsystem.commandvalidation.infrastructure.operationdefinition;

import ai.stapi.graphsystem.commandvalidation.model.ActualValueCommandConstrainViolation;
import ai.stapi.graphsystem.commandvalidation.model.fieldPath.CommandViolationFieldPath;
import org.jetbrains.annotations.Nullable;

public class InvalidComplexTypeCommandViolation extends ActualValueCommandConstrainViolation {

  private final String requiredType;
  private final Throwable cause;

  public InvalidComplexTypeCommandViolation(
      String commandName,
      CommandViolationFieldPath fieldPath,
      Object actualValue,
      String requiredType,
      Throwable cause
  ) {
    super(commandName, fieldPath, Level.ERROR, actualValue);
    this.requiredType = requiredType;
    this.cause = cause;
  }

  @Override
  protected String getBecauseMessage() {
    return "Complex type was not Object. Required type: " + this.requiredType;
  }

  @Nullable
  public Throwable getCause() {
    return cause;
  }

  public String getRequiredType() {
    return requiredType;
  }
}
