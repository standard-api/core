package ai.stapi.graphsystem.dynamiccommandprocessor;

import ai.stapi.graphsystem.messaging.event.AggregateGraphUpdatedEvent;
import ai.stapi.graph.Graph;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.graphsystem.messaging.command.AbstractCommand;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import java.util.List;

public interface SpecificDynamicCommandProcessor {

  default List<? extends AggregateGraphUpdatedEvent<? extends UniqueIdentifier>> processCommand(
      AbstractCommand<? extends UniqueIdentifier> command,
      Graph currentAggregateState
  ) {
    return this.processCommand(command, currentAggregateState,
        MissingFieldResolvingStrategy.STRICT);
  }

  List<? extends AggregateGraphUpdatedEvent<? extends UniqueIdentifier>> processCommand(
      AbstractCommand<? extends UniqueIdentifier> command,
      Graph currentAggregateState,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  );

  boolean supports(AbstractCommand<? extends UniqueIdentifier> command);
}
