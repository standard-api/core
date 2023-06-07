package ai.stapi.graphsystem.commandvalidation.model;

import ai.stapi.graphsystem.messaging.command.Command;
import java.util.List;

public interface CommandValidator {

  List<CommandConstrainViolation> validate(Command command);

}
