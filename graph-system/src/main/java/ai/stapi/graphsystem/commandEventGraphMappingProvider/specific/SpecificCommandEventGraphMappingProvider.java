package ai.stapi.graphsystem.commandEventGraphMappingProvider.specific;

import ai.stapi.graphsystem.messaging.event.AggregateGraphUpdatedEvent;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.graphsystem.messaging.command.AbstractCommand;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import java.util.Map;

public interface SpecificCommandEventGraphMappingProvider {

  Map<Class<? extends AggregateGraphUpdatedEvent<? extends UniqueIdentifier>>, ObjectGraphMapping> provideGraphMapping(
      AbstractCommand<? extends UniqueIdentifier> command
  );

  boolean supports(String serializationType);
}

