package ai.stapi.graphsystem.fixtures.examplegenerators;

import ai.stapi.graphsystem.fixtures.fixtureCommandsGenerator.FileFixtureCommandsGenerator;
import ai.stapi.schema.adHocLoaders.FileLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExampleFileFixtureCommandsGenerator extends FileFixtureCommandsGenerator {

  public ExampleFileFixtureCommandsGenerator(
      ObjectMapper objectMapper,
      FileLoader fileLoader
  ) {
    super(objectMapper, fileLoader);
  }

  @Override
  public float getPriority() {
    return this.PRIORITY_DATA;
  }


  @Override
  public List<String> getFixtureTags() {
    return List.of(
        ExampleFixtureTags.EXAMPLE_FILE_LOADER_TAG
    );
  }

}
