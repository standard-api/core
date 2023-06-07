package ai.stapi.graphsystem.fixtures.fixtureCommandsGenerator;

import ai.stapi.graphsystem.fixtures.examplegenerators.ExampleFixtureCommandsGenerator;
import ai.stapi.graphsystem.fixtures.examplegenerators.ExampleFixtureTags;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ai.stapi.test.integration.IntegrationTestCase;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FixtureCommandsGeneratorTest extends IntegrationTestCase {

  @Test
  void itCanReturnCorrectFixtureCommandGeneratorResult() {
    var generator = new ExampleFixtureCommandsGenerator();
    var actualResult = generator.generate(Set.of());
    this.thenObjectApprovedWithShownIds(actualResult);
  }

  @Test
  void itCanSerializeAndDeserializeDefinedCommands() {
    var generator = new ExampleFixtureCommandsGenerator();
    var actualResult = generator.generate(Set.of());
    var mapper = new ObjectMapper();
    try {
      var serialized = mapper.writeValueAsString(actualResult);
      var deserialized = mapper.readValue(serialized, FixtureCommandsGeneratorResult.class);
      this.thenObjectApprovedWithShownIds(deserialized);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void itShouldReturnDefinedFixtureTags() {
    var generator = new ExampleFixtureCommandsGenerator();
    var actualTags = generator.getFixtureTags();
    Assertions.assertEquals(2, actualTags.size());
    Assertions.assertEquals(ExampleFixtureTags.EXAMPLE_TAG, actualTags.get(0));
    Assertions.assertEquals(ExampleFixtureTags.EXAMPLE_ANOTHER_TAG, actualTags.get(1));
  }
}