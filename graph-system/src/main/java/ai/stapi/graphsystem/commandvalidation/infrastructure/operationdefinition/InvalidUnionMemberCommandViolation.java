package ai.stapi.graphsystem.commandvalidation.infrastructure.operationdefinition;

import ai.stapi.graphsystem.commandvalidation.model.ActualValueCommandConstrainViolation;
import ai.stapi.graphsystem.commandvalidation.model.fieldPath.CommandViolationFieldPath;
import ai.stapi.graphsystem.messaging.command.DynamicCommand;
import org.jetbrains.annotations.Nullable;

public class InvalidUnionMemberCommandViolation extends ActualValueCommandConstrainViolation {

  @Nullable
  private Throwable cause;

  public InvalidUnionMemberCommandViolation(
      String commandName,
      CommandViolationFieldPath fieldPath,
      Object actualValue
  ) {
    super(commandName, fieldPath, Level.ERROR, actualValue);
  }

  public InvalidUnionMemberCommandViolation(
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
    var message = "Union member was invalid. Object with string field under key '%s' is required.";

    return String.format(
        message,
        DynamicCommand.SERIALIZATION_TYPE_FIELD_NAME
    );
  }

  @Nullable
  public Throwable getCause() {
    return cause;
  }
}
