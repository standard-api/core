package ai.stapi.graphsystem.aggregategraphstatemodifier.exceptions;

import ai.stapi.graphsystem.aggregatedefinition.model.CommandHandlerDefinitionDTO;
import ai.stapi.graphsystem.aggregatedefinition.model.EventFactoryModification;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.schema.structureSchema.ComplexStructureType;

public class CannotUpsertToAggregateState extends RuntimeException {

  private CannotUpsertToAggregateState(String becauseMessage) {
    super("Cannot upsert to Aggregate state, because " + becauseMessage);
  }
  
  public static CannotUpsertToAggregateState becauseMainAggregateNodeDoesntExist(
      ComplexStructureType operationStructureType,
      EventFactoryModification modificationDefinition,
      UniqueIdentifier mainNodeId
  ) {
    return new CannotUpsertToAggregateState(
        String.format(
            "main aggregate node doesnt exist.%nOperation name: '%s'%nModification path: '%s'%nMain node id: '%s'",
            operationStructureType.getDefinitionType(),
            modificationDefinition.getModificationPath(),
            mainNodeId.getId()
        )
    );
  }

  public static CannotUpsertToAggregateState becauseModificationDefinitionDoesNotHaveInputValueParameterNameSpecified(
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationStructureType,
      UniqueIdentifier mainNodeId
  ) {
    return new CannotUpsertToAggregateState(
        String.format(
            "modification definition does not have input value parameter name specified.%n" +
                "Operation name: '%s'%nModification path: '%s'%nMain node id: '%s'",
            operationStructureType.getDefinitionType(),
            modificationDefinition.getModificationPath(),
            mainNodeId.getId()
        )
    );
  }
}
