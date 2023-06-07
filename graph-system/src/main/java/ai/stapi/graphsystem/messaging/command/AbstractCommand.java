package ai.stapi.graphsystem.messaging.command;

import ai.stapi.serialization.AbstractSerializableObject;
import ai.stapi.identity.UniqueIdentifier;

public abstract class AbstractCommand<T extends UniqueIdentifier> extends AbstractSerializableObject
    implements EndpointCommand {

  private T targetIdentifier;

  protected AbstractCommand() {
  }

  protected AbstractCommand(
      T targetIdentifier,
      String serializationType
  ) {
    super(serializationType);
    this.targetIdentifier = targetIdentifier;
  }

  public T getTargetIdentifier() {
    return targetIdentifier;
  }
}
