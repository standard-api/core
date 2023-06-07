package ai.stapi.graphsystem.commandvalidation.model.exceptions;

public class CannotValidateCommand extends RuntimeException {

  private CannotValidateCommand(String commandName, String becauseMessage) {
    super(String.format("Cannot validate command: '%s', because %s", commandName, becauseMessage));
  }

  private CannotValidateCommand(String commandName, String becauseMessage, Throwable cause) {
    super(
        String.format("Cannot validate command: '%s', because %s", commandName, becauseMessage),
        cause
    );
  }

  public static CannotValidateCommand becauseThereWasNoOperationDefinition(String commandName) {
    return new CannotValidateCommand(
        commandName,
        "there was no Operation Definition to be found."
    );
  }

  public static CannotValidateCommand becauseThereWasNoOperationDefinition(
      String commandName,
      Throwable cause
  ) {
    return new CannotValidateCommand(
        commandName,
        "there was no Operation Definition to be found.",
        cause
    );
  }
}
