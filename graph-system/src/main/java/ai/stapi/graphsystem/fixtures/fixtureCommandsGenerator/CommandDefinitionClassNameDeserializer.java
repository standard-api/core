package ai.stapi.graphsystem.fixtures.fixtureCommandsGenerator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ai.stapi.graphsystem.messaging.command.Command;
import java.io.IOException;

public class CommandDefinitionClassNameDeserializer extends StdDeserializer<CommandDefinition> {

  public CommandDefinitionClassNameDeserializer() {
    this(null);
  }

  protected CommandDefinitionClassNameDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public CommandDefinition deserialize(
      JsonParser jsonParser,
      DeserializationContext deserializationContext
  ) throws IOException, JsonProcessingException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    var commandClassName = node.get("name").asText();
    try {
      var commandClazz = Class.forName(commandClassName);
      var serializedCommand = node.get("command").toString();
      var command = (Command) new ObjectMapper().readValue(serializedCommand, commandClazz);
      return new CommandDefinition(command);

    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

  }
}
