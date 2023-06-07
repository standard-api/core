package ai.stapi.graphsystem.fixtures.fixtureCommandsGenerator;

import ai.stapi.graphsystem.messaging.command.Command;
import java.util.List;
import java.util.Set;

public abstract class SimpleFixtureCommandsGenerator extends AbstractFixtureCommandsGenerator {

  protected abstract List<Command> getCommands();

  @Override
  public FixtureCommandsGeneratorResult generate(Set<String> processedFileNames) {
    return new FixtureCommandsGeneratorResult(
        this.getClass(),
        this.getCommands()
    );
  }

}
