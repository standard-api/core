package ai.stapi.graphsystem.aggregategraphstatemodifier;

import ai.stapi.graphsystem.aggregatedefinition.model.AggregateDefinitionProvider;
import ai.stapi.graphsystem.aggregatedefinition.model.CommandHandlerDefinitionDTO.EventFactory;
import ai.stapi.graphsystem.dynamiccommandprocessor.SpecificDynamicCommandProcessor;
import ai.stapi.graphsystem.messaging.event.AggregateGraphUpdatedEvent;
import ai.stapi.graphsystem.messaging.command.DynamicCommand;
import ai.stapi.graphsystem.messaging.event.DynamicGraphUpdatedEvent;
import ai.stapi.graph.Graph;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.graphsystem.messaging.command.AbstractCommand;
import ai.stapi.graphoperations.objectGraphMapper.model.GraphMappingResult;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import java.util.List;

public class DynamicAggregateCommandProcessor implements SpecificDynamicCommandProcessor {

  private final AggregateDefinitionProvider aggregateDefinitionProvider;
  private final GenericAggregateGraphStateModificator genericAggregateGraphStateModificator;

  public DynamicAggregateCommandProcessor(
      AggregateDefinitionProvider aggregateDefinitionProvider,
      GenericAggregateGraphStateModificator genericAggregateGraphStateModificator
  ) {
    this.aggregateDefinitionProvider = aggregateDefinitionProvider;
    this.genericAggregateGraphStateModificator = genericAggregateGraphStateModificator;
  }

  @Override
  public List<? extends AggregateGraphUpdatedEvent<? extends UniqueIdentifier>> processCommand(
      AbstractCommand<? extends UniqueIdentifier> command,
      Graph currentAggregateState,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  ) {
    var aggregateDefinition = this.aggregateDefinitionProvider.provideAll()
        .stream()
        .filter(
            aggregateDefinitionDTO ->
                aggregateDefinitionDTO.getCommand()
                    .stream()
                    .anyMatch(
                        commandHandlerDefinitionDTO ->
                            commandHandlerDefinitionDTO.getOperation().getId()
                                .equals(command.getSerializationType())
                    )
        ).findFirst()
        .orElseThrow();

    var commandHandlerDefinition = aggregateDefinition.getCommand()
        .stream()
        .filter(
            commandHandlerDefinitionDTO -> commandHandlerDefinitionDTO.getOperation()
                .getId()
                .equals(command.getSerializationType())
        ).findFirst()
        .orElseThrow();

    var operationDefinition = commandHandlerDefinition.getOperation();
    return commandHandlerDefinition.getEventFactory()
        .stream()
        .map(eventFactory -> this.createEvent(
            command,
            currentAggregateState,
            aggregateDefinition.getStructure().getId(),
            eventFactory,
            operationDefinition,
            missingFieldResolvingStrategy
        )).toList();
  }

  private DynamicGraphUpdatedEvent createEvent(
      AbstractCommand<? extends UniqueIdentifier> command,
      Graph currentAggregateState,
      String aggregateType,
      EventFactory eventFactory,
      OperationDefinitionDTO operationDefinition,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  ) {
    var graphMappingResult = eventFactory.getModification().stream()
        .map(modification -> this.genericAggregateGraphStateModificator.modify(
            aggregateType,
            currentAggregateState,
            (DynamicCommand) command,
            modification,
            operationDefinition,
            missingFieldResolvingStrategy
        )).reduce(GraphMappingResult::merge)
        .orElse(new GraphMappingResult(
            new Graph(),
            List.of()
        ));

    return new DynamicGraphUpdatedEvent(
        eventFactory.getEvent().getId(),
        command.getTargetIdentifier(),
        graphMappingResult.getGraph(),
        graphMappingResult.getElementForRemoval()
    );
  }

  @Override
  public boolean supports(AbstractCommand<? extends UniqueIdentifier> command) {
    return this.aggregateDefinitionProvider.provideAll()
        .stream()
        .anyMatch(
            aggregateDefinitionDTO -> aggregateDefinitionDTO.getCommand()
                .stream()
                .anyMatch(commandHandlerDefinitionDTO -> commandHandlerDefinitionDTO.getOperation()
                    .getId().equals(command.getSerializationType()))
        );
  }
}
