package ai.stapi.graphsystem.commandvalidation.infrastructure.operationdefinition;

import ai.stapi.graphsystem.commandvalidation.model.AbstractCommandConstrainViolation;
import ai.stapi.graphsystem.commandvalidation.model.fieldPath.CommandViolationFieldPath;

public class MissingRequiredFieldCommandViolation extends AbstractCommandConstrainViolation {

  public MissingRequiredFieldCommandViolation(
      String commandName,
      CommandViolationFieldPath fieldPath
  ) {
    super(commandName, fieldPath, Level.ERROR);
  }

  @Override
  protected String getBecauseMessage() {
    return "Required field is missing.";
  }
}
