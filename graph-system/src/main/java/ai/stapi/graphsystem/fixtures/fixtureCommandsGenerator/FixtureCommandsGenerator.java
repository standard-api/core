package ai.stapi.graphsystem.fixtures.fixtureCommandsGenerator;

import java.util.List;
import java.util.Set;

public interface FixtureCommandsGenerator {

  float PRIORITY_STRUCTURES = 1000;
  float PRIORITY_DATA = 100;

  List<String> getFixtureTags();

  String getGeneratorName();

  FixtureCommandsGeneratorResult generate(Set<String> processedFileNames);

  float getPriority();
}
