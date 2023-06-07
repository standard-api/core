package ai.stapi.graphsystem.commandvalidation.infrastructure.operationdefinition;

import ai.stapi.graphsystem.commandvalidation.model.AbstractCommandConstrainViolation;
import ai.stapi.graphsystem.commandvalidation.model.fieldPath.CommandViolationFieldPath;

public class ExtraFieldCommandViolation extends AbstractCommandConstrainViolation {

  public ExtraFieldCommandViolation(String commandName, CommandViolationFieldPath fieldPath) {
    super(commandName, fieldPath, Level.WARNING);
  }

  @Override
  protected String getBecauseMessage() {
    return "It is extra field.";
  }
}
