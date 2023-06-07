package ai.stapi.graphsystem.commandvalidation.model.exceptions;

import ai.stapi.graphsystem.commandvalidation.model.CommandConstrainViolation;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CannotDispatchCommand extends RuntimeException {

  private final transient List<CommandConstrainViolation> commandConstrainViolations;

  private CannotDispatchCommand(
      String commandName,
      String becauseMessage,
      List<CommandConstrainViolation> commandConstrainViolations
  ) {
    super(
        String.format(
            "Cannot dispatch Command '%s', %s",
            commandName,
            becauseMessage
        )
    );
    this.commandConstrainViolations = commandConstrainViolations;
  }

  public static CannotDispatchCommand becauseThereWereConstrainViolation(
      String commandName,
      String targetAggregateIdentifier,
      List<CommandConstrainViolation> commandConstrainViolations
  ) {
    return new CannotDispatchCommand(
        commandName,
        ("""
            because there were constrain violations.
            Target Aggregate Identifier: '%s'
            
            %s
            """
        ).formatted(
            targetAggregateIdentifier,
            commandConstrainViolations.stream()
                .sorted(Comparator.comparing(CommandConstrainViolation::getLevel))
                .map(CommandConstrainViolation::getMessage)
                .collect(Collectors.joining("\n\n"))
        ),
        commandConstrainViolations
    );
  }

  public static CannotDispatchCommand becauseItDoesNotImplementCommandInterface(
      String commandName,
      Class<?> actualClass
  ) {
    return new CannotDispatchCommand(
        commandName,
        "because it does not implement Command interface. Actual className: "
            + actualClass.getSimpleName(),
        List.of()
    );
  }

  public List<CommandConstrainViolation> getCommandConstrainViolations() {
    return commandConstrainViolations;
  }
}
