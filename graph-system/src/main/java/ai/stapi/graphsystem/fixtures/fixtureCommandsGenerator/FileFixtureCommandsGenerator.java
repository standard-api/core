package ai.stapi.graphsystem.fixtures.fixtureCommandsGenerator;

import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.graphsystem.messaging.command.DynamicCommand;
import ai.stapi.graphsystem.messaging.command.Command;
import ai.stapi.graphsystem.structuredefinition.command.importStructureDefinitionFromSource.ImportStructureDefinition;
import ai.stapi.schema.structuredefinition.RawStructureDefinitionData;
import ai.stapi.schema.adHocLoaders.FileLoader;
import ai.stapi.schema.structuredefinition.StructureDefinitionId;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.io.Resource;

public abstract class FileFixtureCommandsGenerator extends AbstractFixtureCommandsGenerator {

  private final FileLoader fileLoader;
  private final ObjectMapper objectMapper;

  protected FileFixtureCommandsGenerator(
      ObjectMapper objectMapper,
      FileLoader fileLoader
  ) {
    this.fileLoader = fileLoader;
    this.objectMapper = objectMapper;
  }

  @Nullable
  private static FixtureFileInfo getFixtureFileInfo(Resource resource) throws IOException {
    String fileUri = resource.getURI().toString();
    String[] parts = fileUri.split("/");
    if (parts.length < 2) {
      return null;
    }
    String commandName = parts[parts.length - 2];
    String filename = commandName + "/" + parts[parts.length - 1];
    return new FixtureFileInfo(filename, commandName);
  }

  @Override
  public FixtureCommandsGeneratorResult generate(Set<String> alreadyProcessedFileNames) {
    return new FixtureCommandsGeneratorResult(
        this.getClass(),
        this.loadCommandsFromFiles(alreadyProcessedFileNames),
        alreadyProcessedFileNames
    );
  }

  protected List<Command> loadCommandsFromFiles(Set<String> processedFileNames) {
    var list = this.fileLoader.loadFixtureFiles(
            this.getClass(),
            processedFileNames
        ).stream()
        .sorted(Comparator.comparing(Resource::getFilename))
        .map(this::mapJsonFileToCommand)
        .toList();

    return this.sortCommands(list);
  }

  protected List<Command> sortCommands(List<Command> loadCommandsFromFiles) {
    return new ArrayList<>(loadCommandsFromFiles);
  }

  protected Command mapJsonFileToCommand(Resource resource) {

    byte[] content;
    try {
      InputStream input = resource.getInputStream();
      content = IOUtils.toByteArray(input);
    } catch (IOException exception) {
      try {
        throw new RuntimeException(
            String.format(
                "Unable to read resource file of URI %s",
                resource.getURI()
            ),
            exception
        );
      } catch (IOException ex) {
        throw new RuntimeException(
            "Unable to read resource file and not even its URI.",
            exception
        );
      }
    }

    try {

      var commandJsonPayload = this.objectMapper.readValue(
          content,
          LinkedHashMap.class
      );

      var fileInfo = getFixtureFileInfo(resource);

      Command command;
      /*
       TODO : this if for structure definition should vanish when we will have properly dynamic
        aggregates including this one
      */
      if (Objects.requireNonNull(fileInfo).commandType.equals("ImportStructureDefinition")) {

        var dto = this.objectMapper.convertValue(commandJsonPayload, RawStructureDefinitionData.class);
        command = new ImportStructureDefinition(
            new StructureDefinitionId(
                (String) commandJsonPayload.get("id")
            ),
            dto
        );
      } else {
        command = new DynamicCommand(
            new UniqueIdentifier((String) commandJsonPayload.get("id")),
            fileInfo.getCommandType(),
            commandJsonPayload
        );
      }

      return command;
    } catch (IOException exception) {
      try {
        throw new RuntimeException(
            String.format(
                "Unable to deserialize bytecode content of resource file of URI: %s",
                resource.getURI()
            ),
            exception
        );
      } catch (IOException ex) {
        throw new RuntimeException(
            "Unable to read resource file and not even determine its URI.",
            exception
        );
      }

    }
  }

  private static class FixtureFileInfo {

    private final String fullName;
    private final String commandType;

    public FixtureFileInfo(String fullName, String commandType) {
      this.fullName = fullName;
      this.commandType = commandType;
    }

    public String getFullName() {
      return fullName;
    }

    public String getCommandType() {
      return commandType;
    }
  }


}
