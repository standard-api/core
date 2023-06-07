package ai.stapi.graphsystem.commandEventGraphMappingProvider.exampleImplementations;

import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.graphsystem.messaging.command.AbstractCommand;

public class ExampleNotSupportedCommand extends AbstractCommand<UniqueIdentifier> {

  public static final String SERIALIZATION_TYPE = "31060896-7aaa-43d1-bc51-36584ea31270";

  public ExampleNotSupportedCommand() {
    super(new UniqueIdentifier("not_supported_command"), SERIALIZATION_TYPE);
  }
}
