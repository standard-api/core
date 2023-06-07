package ai.stapi.graphsystem.fixtures.fixtureCommandsGenerator;

import ai.stapi.schema.adHocLoaders.FileLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import ai.stapi.graphsystem.messaging.command.DynamicCommand;
import ai.stapi.graphsystem.messaging.command.Command;
import ai.stapi.graphsystem.structuredefinition.command.importStructureDefinitionFromSource.ImportStructureDefinition;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractModelFileFixtureGenerator extends FileFixtureCommandsGenerator {

  protected AbstractModelFileFixtureGenerator(
      ObjectMapper objectMapper,
      FileLoader fileLoader
  ) {
    super(objectMapper, fileLoader);
  }

  private static boolean isKind(Command command, String filterType) {
    if (command instanceof DynamicCommand dynamicCommand) {
      return dynamicCommand.getData().get("kind").equals(filterType);
    } else {
      if (command instanceof ImportStructureDefinition importStructureDefinition) {
        return importStructureDefinition.getStructureDefinitionSource()
            .getKind()
            .equals(filterType);
      }
      return false;
    }
  }

  @Override
  protected List<Command> sortCommands(List<Command> list) {
    var sortedList = new ArrayList<Command>();
    var importStructuresCommand = list.stream()
        .filter(ImportStructureDefinition.class::isInstance)
        .map(ImportStructureDefinition.class::cast)
        .toList();

    sortedList.addAll(
        importStructuresCommand.stream()
            .filter(command -> AbstractModelFileFixtureGenerator.isKind(
                command,
                "primitive-type"
            ))
            .toList()
    );

    sortedList.addAll(
        importStructuresCommand.stream()
            .filter(command -> AbstractModelFileFixtureGenerator.isKind(
                command,
                "complex-type"
            ))
            .toList()
    );

    sortedList.addAll(
        importStructuresCommand.stream()
            .filter(command -> AbstractModelFileFixtureGenerator.isKind(
                command,
                "resource"
            ))
            .toList()
    );

    var rest = list.stream()
        .filter(command -> !(command instanceof ImportStructureDefinition))
        .toList();

    sortedList.addAll(rest);
    return sortedList;
  }
}
