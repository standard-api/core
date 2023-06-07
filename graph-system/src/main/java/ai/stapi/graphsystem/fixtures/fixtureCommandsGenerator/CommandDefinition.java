package ai.stapi.graphsystem.fixtures.fixtureCommandsGenerator;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ai.stapi.graphsystem.messaging.command.DynamicCommand;
import ai.stapi.graphsystem.messaging.command.Command;
import java.io.Serializable;

@JsonDeserialize(using = CommandDefinitionClassNameDeserializer.class)
public class CommandDefinition implements Serializable {

  private Command command;
  private String name;

  private CommandDefinition() {
  }

  public CommandDefinition(Command command) {
    this.command = command;
    if (command instanceof DynamicCommand dynamicCommand) {
      this.name = dynamicCommand.getSerializationType();
    } else {
      this.name = command.getClass().getName();
    }
  }

  public Command getCommand() {
    return command;
  }

  public String getName() {
    return name;
  }

}
