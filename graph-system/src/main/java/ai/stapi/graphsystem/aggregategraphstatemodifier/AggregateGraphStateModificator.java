package ai.stapi.graphsystem.aggregategraphstatemodifier;

import ai.stapi.graph.Graph;
import ai.stapi.graphsystem.aggregatedefinition.model.CommandHandlerDefinitionDTO.EventFactory.EventFactoryModification;
import ai.stapi.graphsystem.messaging.command.DynamicCommand;
import ai.stapi.graphoperations.objectGraphMapper.model.GraphMappingResult;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.schema.structureSchema.ComplexStructureType;

public interface AggregateGraphStateModificator {

  GraphMappingResult modify(
      String aggregateType,
      Graph currentAggregateState,
      DynamicCommand command,
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationStructureType,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  );

  boolean supports(EventFactoryModification modificationDefinition);
}
