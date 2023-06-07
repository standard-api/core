package ai.stapi.graphsystem.messaging.command;

import ai.stapi.identity.UniqueIdentifier;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPayloadCommand<T extends UniqueIdentifier>
    extends AbstractCommand<T> {

  private Map<String, Object> data;

  protected AbstractPayloadCommand() {
    super();
  }

  protected AbstractPayloadCommand(
      String serializationType,
      T uniqueIdentifier,
      Map<String, Object> definition
  ) {
    super(
        uniqueIdentifier,
        serializationType
    );
    this.data = new HashMap<>(definition);
  }

  public Map<String, Object> getData() {
    return data;
  }
}
