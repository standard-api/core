package ai.stapi.graphsystem.systemfixtures.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ai.stapi.graphsystem.fixtures.fixtureCommandsGenerator.AbstractModelFileFixtureGenerator;
import ai.stapi.schema.adHocLoaders.FileLoader;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

public class SystemModelFixtureGenerator extends AbstractModelFileFixtureGenerator {

  public static final String TAG = "system-model";

  public SystemModelFixtureGenerator(
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
