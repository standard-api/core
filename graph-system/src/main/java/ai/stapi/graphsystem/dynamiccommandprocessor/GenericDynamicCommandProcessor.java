package ai.stapi.graphsystem.dynamiccommandprocessor;

import ai.stapi.graphsystem.messaging.event.AggregateGraphUpdatedEvent;
import ai.stapi.graph.Graph;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.graphsystem.messaging.command.AbstractCommand;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GenericDynamicCommandProcessor implements DynamicCommandProcessor {

  private final List<SpecificDynamicCommandProcessor> specificDynamicCommandProcessors;

  public GenericDynamicCommandProcessor(
      List<SpecificDynamicCommandProcessor> specificDynamicCommandProcessors
  ) {
    this.specificDynamicCommandProcessors = specificDynamicCommandProcessors;
  }

  @Override
  public List<? extends AggregateGraphUpdatedEvent<? extends UniqueIdentifier>> processCommand(
      AbstractCommand<? extends UniqueIdentifier> command,
      Graph currentAggregateState,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  ) {
    var supporting = this.getSupportingProcessor(command);
    return supporting.processCommand(command, currentAggregateState, missingFieldResolvingStrategy);
  }

  private SpecificDynamicCommandProcessor getSupportingProcessor(
      AbstractCommand<? extends UniqueIdentifier> command
  ) {
    var supporting = this.specificDynamicCommandProcessors.stream()
        .filter(specific -> specific.supports(command))
        .toList();

    if (supporting.size() != 1) {
      throw new NoSupportingSpecificDynamicCommandProcessor(
          String.format(
              "Command cannot be processed, because there is no supporting command processor.%n" +
                  "Command type: '%s'" +
                  "Target aggregate identifier: '%s'",
              command.getSerializationType(),
              command.getTargetIdentifier()
          )
      );
    }
    return supporting.get(0);
  }

  private static class NoSupportingSpecificDynamicCommandProcessor extends RuntimeException {

    public NoSupportingSpecificDynamicCommandProcessor(String message) {
      super(message);
    }
  }
}
