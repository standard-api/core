package ai.stapi.graphsystem.messaging.command;

import ai.stapi.identity.UniqueIdentifier;
import java.util.Map;

public class DynamicCommand extends AbstractPayloadCommand<UniqueIdentifier> {

  public static final String SERIALIZATION_TYPE_FIELD_NAME = "serializationType";
  public static final String TARGET_IDENTIFIER_FIELD_NAME = "targetIdentifier";

  private DynamicCommand() {
  }

  public DynamicCommand(
      UniqueIdentifier targetIdentifier,
      String serializationType,
      Map<String, Object> data
  ) {
    super(serializationType, targetIdentifier, data);
  }
}
