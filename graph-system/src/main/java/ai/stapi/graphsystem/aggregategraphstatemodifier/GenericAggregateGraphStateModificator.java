package ai.stapi.graphsystem.aggregategraphstatemodifier;

import ai.stapi.graphsystem.aggregatedefinition.model.EventFactoryModification;
import ai.stapi.graphsystem.aggregategraphstatemodifier.exceptions.CannotModifyAggregateState;
import ai.stapi.graphsystem.messaging.command.DynamicCommand;
import ai.stapi.graph.Graph;
import ai.stapi.graphoperations.objectGraphMapper.model.GraphMappingResult;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionStructureTypeMapper;
import java.util.List;
import org.springframework.stereotype.Service;

public class GenericAggregateGraphStateModificator {

  private final List<AggregateGraphStateModificator> aggregateGraphStateModificators;
  private final OperationDefinitionStructureTypeMapper operationDefinitionStructureTypeMapper;

  public GenericAggregateGraphStateModificator(
      List<AggregateGraphStateModificator> aggregateGraphStateModificators,
      OperationDefinitionStructureTypeMapper operationDefinitionStructureTypeMapper
  ) {
    this.aggregateGraphStateModificators = aggregateGraphStateModificators;
    this.operationDefinitionStructureTypeMapper = operationDefinitionStructureTypeMapper;
  }

  public GraphMappingResult modify(
      String aggregateType,
      Graph currentAggregateState,
      DynamicCommand command,
      EventFactoryModification modificationDefinition,
      OperationDefinitionDTO operationDefinitionDTO,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  ) {
    var supporting = this.aggregateGraphStateModificators.stream()
        .filter(modificator -> modificator.supports(modificationDefinition))
        .toList();

    if (supporting.size() != 1) {
      throw CannotModifyAggregateState.becauseThereWasNotExactlyOneModificatorForDefinition(
          modificationDefinition,
          operationDefinitionDTO
      );
    }
    return supporting.get(0).modify(
        aggregateType,
        currentAggregateState,
        command,
        modificationDefinition,
        this.operationDefinitionStructureTypeMapper.map(operationDefinitionDTO),
        missingFieldResolvingStrategy
    );
  }
}
