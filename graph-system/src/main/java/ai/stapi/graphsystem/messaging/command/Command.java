package ai.stapi.graphsystem.messaging.command;

import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.serialization.SerializableObject;

public interface Command extends SerializableObject {

  UniqueIdentifier getTargetIdentifier();
}
