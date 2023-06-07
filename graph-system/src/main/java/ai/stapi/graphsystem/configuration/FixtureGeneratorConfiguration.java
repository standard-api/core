package ai.stapi.graphsystem.configuration;

import ai.stapi.graphsystem.fixtures.fixtureCommandsGenerator.FixtureCommandsGenerator;
import ai.stapi.graphsystem.fixtures.fixtureCommandsGenerator.GenericFixtureCommandsGenerator;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class FixtureGeneratorConfiguration {
  
  @Bean
  public GenericFixtureCommandsGenerator genericFixtureCommandsGenerator(
      List<FixtureCommandsGenerator> fixtureCommandsGenerators
  ) {
    return new GenericFixtureCommandsGenerator(fixtureCommandsGenerators);
  }
  
  
}
