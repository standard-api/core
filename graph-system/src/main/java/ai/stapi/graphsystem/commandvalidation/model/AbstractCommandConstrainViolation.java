package ai.stapi.graphsystem.commandvalidation.model;

import ai.stapi.graphsystem.commandvalidation.model.fieldPath.CommandViolationFieldPath;

public abstract class AbstractCommandConstrainViolation implements CommandConstrainViolation {

  private final String commandName;

  private final CommandViolationFieldPath fieldPath;
  private final Level level;

  protected AbstractCommandConstrainViolation(
      String commandName,
      CommandViolationFieldPath fieldPath,
      Level level
  ) {
    this.commandName = commandName;
    this.fieldPath = fieldPath;
    this.level = level;
  }

  protected abstract String getBecauseMessage();

  @Override
  public String getCommandName() {
    return commandName;
  }

  @Override
  public CommandViolationFieldPath getFieldPath() {
    return fieldPath;
  }

  @Override
  public String getMessage() {
    return String.format(
        "[%s] Command with name: '%s' did not validate.\nField path: %s\n%s",
        this.level,
        this.commandName,
        this.renderFieldPath(),
        this.getBecauseMessage()
    );
  }

  @Override
  public Level getLevel() {
    return level;
  }

  private String renderFieldPath() {
    return this.commandName + this.fieldPath.renderPath();
  }
}
