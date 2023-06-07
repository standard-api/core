package ai.stapi.graphsystem.aggregatedefinition.model;

import ai.stapi.graphsystem.aggregatedefinition.model.exceptions.CannotProvideAggregateDefinition;
import java.util.List;

public interface AggregateDefinitionProvider {

  List<AggregateDefinitionDTO> provideAll();

  AggregateDefinitionDTO provide(String aggregateType) throws CannotProvideAggregateDefinition;

  default AggregateDefinitionDTO getAggregateForOperation(
      String operationDefinitionId
  ) throws CannotProvideAggregateDefinition {
    var foundAggregates = this.provideAll().stream()
        .filter(aggregate -> aggregate.containsOperation(operationDefinitionId))
        .toList();

    if (foundAggregates.isEmpty()) {
      throw CannotProvideAggregateDefinition.becauseThereIsNoCommandHandlerForOperation(
          operationDefinitionId
      );
    }
    return foundAggregates.get(0);
  }

  default boolean containsAggregateForOperation(String operationDefinitionId) {
    return this.provideAll().stream().anyMatch(
        aggregate -> aggregate.containsOperation(operationDefinitionId)
    );
  }
}
