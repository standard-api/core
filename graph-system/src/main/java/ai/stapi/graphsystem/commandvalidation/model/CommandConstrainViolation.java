package ai.stapi.graphsystem.commandvalidation.model;

import ai.stapi.graphsystem.commandvalidation.model.fieldPath.CommandViolationFieldPath;

public interface CommandConstrainViolation {

  String getCommandName();

  CommandViolationFieldPath getFieldPath();

  String getMessage();

  Level getLevel();

  enum Level {
    ERROR,
    WARNING
  }
}
