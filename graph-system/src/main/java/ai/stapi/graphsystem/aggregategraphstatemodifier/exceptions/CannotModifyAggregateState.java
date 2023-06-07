package ai.stapi.graphsystem.aggregategraphstatemodifier.exceptions;

import ai.stapi.graphsystem.aggregatedefinition.model.CommandHandlerDefinitionDTO.EventFactory.EventFactoryModification;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;

public class CannotModifyAggregateState extends RuntimeException {

  private CannotModifyAggregateState(String becauseMessage) {
    super("Cannot modify aggregate state, because " + becauseMessage);
  }

  public static CannotModifyAggregateState becauseThereWasNotExactlyOneModificatorForDefinition(
      EventFactoryModification modificationDefinition,
      OperationDefinitionDTO operationDefinition
  ) {
    return new CannotModifyAggregateState(
        String.format(
            "there was not exactly one modificator for specified modification definition.%n" +
                "Modification kind: '%s'%nOperation name: '%s'",
            modificationDefinition.getKind(),
            operationDefinition.getId()
        )
    );
  }
}
