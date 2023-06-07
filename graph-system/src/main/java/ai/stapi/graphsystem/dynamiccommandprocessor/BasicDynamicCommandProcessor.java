package ai.stapi.graphsystem.dynamiccommandprocessor;

import ai.stapi.graphsystem.messaging.event.AggregateGraphUpdatedEvent;
import ai.stapi.graphsystem.genericGraphEventFactory.GenericGraphEventFactory;
import ai.stapi.graph.Graph;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.graphsystem.messaging.command.AbstractCommand;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.graphsystem.commandEventGraphMappingProvider.GenericCommandEventGraphMappingProvider;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BasicDynamicCommandProcessor implements SpecificDynamicCommandProcessor {

  protected final GenericObjectGraphMapper objectGraphMapper;
  protected final GenericGraphEventFactory genericEventFactory;
  protected final GenericCommandEventGraphMappingProvider commandEventGraphMappingProvider;


  public BasicDynamicCommandProcessor(
      GenericObjectGraphMapper objectGraphMapper,
      GenericGraphEventFactory genericEventFactory,
      GenericCommandEventGraphMappingProvider commandEventGraphMappingProvider
  ) {
    this.objectGraphMapper = objectGraphMapper;
    this.genericEventFactory = genericEventFactory;
    this.commandEventGraphMappingProvider = commandEventGraphMappingProvider;
  }

  @Override
  public List<? extends AggregateGraphUpdatedEvent<? extends UniqueIdentifier>> processCommand(
      AbstractCommand<? extends UniqueIdentifier> command,
      Graph currentAggregateState,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  ) {
    var eventMappings = this.commandEventGraphMappingProvider.provideGraphMappings(command);
    return eventMappings.entrySet().stream()
        .map(entry -> {
          var result = this.objectGraphMapper.mapToGraph(
              entry.getValue(),
              command,
              missingFieldResolvingStrategy
          );
          return this.genericEventFactory.createEvent(
              entry.getKey(),
              command.getTargetIdentifier(),
              result.getGraph(),
              result.getElementForRemoval()
          );
        }).toList();
  }

  @Override
  public boolean supports(AbstractCommand<? extends UniqueIdentifier> command) {
    return this.commandEventGraphMappingProvider.containsFor(command);
  }
}
