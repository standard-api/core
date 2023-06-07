package ai.stapi.graphsystem.fixtures.fixtureCommandsGenerator;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenericFixtureCommandsGenerator {

  private final List<FixtureCommandsGenerator> fixtureCommandsGenerators;

  @Autowired
  public GenericFixtureCommandsGenerator(List<FixtureCommandsGenerator> fixtureCommandsGenerators) {
    this.fixtureCommandsGenerators = fixtureCommandsGenerators;
  }

  public Stream<FixtureCommandsGeneratorResult> generate(
      List<String> tags,
      List<String> processedGenerators,
      Set<String> processedFileNames,
      float minPriority,
      float maxPriority
  ) {
    var fixtureCommandResults = this.fixtureCommandsGenerators.stream()
        .filter(fixtureCommandsGenerator ->
            fixtureCommandsGenerator.getPriority() >= minPriority
                && fixtureCommandsGenerator.getPriority() <= maxPriority
        )
        .sorted(Comparator.comparingDouble(FixtureCommandsGenerator::getPriority).reversed())
        .filter((generator) -> generator.getFixtureTags().stream().anyMatch(tags::contains)
            && !processedGenerators.contains(generator.getGeneratorName()))
        .map(fixtureCommandsGenerator -> fixtureCommandsGenerator.generate(processedFileNames))
        .toList();
    return fixtureCommandResults.stream();
  }
}
