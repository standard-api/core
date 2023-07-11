package ai.stapi.formapi.formmapper.fixtures;

import ai.stapi.graphsystem.fixtures.fixtureCommandsGenerator.FileFixtureCommandsGenerator;
import ai.stapi.schema.adHocLoaders.FileLoader;
import ai.stapi.test.schemaintegration.StructureDefinitionScope;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.List;

@StructureDefinitionScope(FormApiTestDefinitionsLoader.SCOPE)
public class FormApiTestFixtureGenerator extends FileFixtureCommandsGenerator {

  public static final String TAG = "system-model";

  public FormApiTestFixtureGenerator(
      FileLoader fileLoader
  ) {
    super(
        new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES),
        fileLoader
    );
  }

  @Override
  public float getPriority() {
    return PRIORITY_STRUCTURES;
  }

  @Override
  public List<String> getFixtureTags() {
    return List.of(TAG);
  }

}
