package ai.stapi.graphsystem.fixtures.fixtureCommandsGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ai.stapi.graphsystem.messaging.command.Command;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FixtureCommandsGeneratorResult implements Serializable {

  private String generatorClassName;
  private List<CommandDefinition> commandDefinitions;
  private boolean oneTimeGenerator = false;
  private Set<String> processedFiles;

  private FixtureCommandsGeneratorResult() {
  }

  public FixtureCommandsGeneratorResult(
      Class<? extends FixtureCommandsGenerator> generatorClassName,
      List<Command> commands
  ) {
    this.generatorClassName = generatorClassName.getName();
    this.commandDefinitions =
        commands.stream().map(CommandDefinition::new).collect(Collectors.toList());
    this.processedFiles = new HashSet<>();
    this.oneTimeGenerator = true;
  }

  public FixtureCommandsGeneratorResult(
      Class<? extends FixtureCommandsGenerator> generatorClassName,
      List<Command> commands,
      Set<String> processedFiles
  ) {
    this.generatorClassName = generatorClassName.getName();
    this.commandDefinitions =
        commands.stream().map(CommandDefinition::new).collect(Collectors.toList());
    this.processedFiles = processedFiles;
    this.oneTimeGenerator = false;
  }

  public String getGeneratorClassName() {
    return generatorClassName;
  }

  @JsonIgnore
  public List<Command> getCommands() {
    return commandDefinitions.stream().map(CommandDefinition::getCommand)
        .collect(Collectors.toList());
  }

  public List<CommandDefinition> getCommandDefinitions() {
    return commandDefinitions;
  }

  public boolean isOneTimeGenerator() {
    return oneTimeGenerator;
  }

  public Set<String> getProcessedFiles() {
    return this.processedFiles;
  }
}
