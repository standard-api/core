package ai.stapi.graphsystem.aggregategraphstatemodifier.exceptions;

import ai.stapi.graphsystem.aggregatedefinition.model.EventFactoryModification;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.schema.structureSchema.ComplexStructureType;

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

  public static CannotModifyAggregateState becauseThereIsNoIdInCommandAtStartIdParameterName(
      String startIdParameterName,
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationStructureType
  ) {
    return new CannotModifyAggregateState(
        String.format(
            "there is no start id value in command at specified start id parameter name. " +
                "Probably there is a mistake in modification definition.%n" +
                "Operation name: '%s'%nModification kind: '%s'%n"
                + "Start id parameter name: '%s'",
            operationStructureType.getDefinitionType(),
            modificationDefinition.getKind(),
            startIdParameterName
        )
    );
  }

  public static CannotModifyAggregateState becauseStartIdIsNotOfCorrectFormat(
      String startIdValue,
      String startIdParameterName,
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationStructureType
  ) {
    return new CannotModifyAggregateState(
        String.format(
            "start id found at start id parameter name is not of correct format (Type/Id).%n" +
                "Operation name: '%s'%nModification kind: '%s'%n"
                + "Start id parameter name: '%s'%nStart id value: '%s'",
            operationStructureType.getDefinitionType(),
            modificationDefinition.getKind(),
            startIdParameterName,
            startIdValue
        )
    );
  }

  public static CannotModifyAggregateState becauseThereIsNoNodeWithIdSpecifiedAtStartIdParameterName(
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationStructureType,
      UniqueIdentifier id,
      String startIdParameterName
  ) {
    return new CannotModifyAggregateState(
        String.format(
            "there is no node with id specified at start id parameter name.%n" +
                "Operation name: '%s'%nId: '%s'%nModification kind: '%s'%n"
                + "Start id parameter name: '%s'",
            operationStructureType.getDefinitionType(),
            id.getId(),
            modificationDefinition.getKind(),
            startIdParameterName
        )
    );
  }

  public static CannotModifyAggregateState becauseThereAreEdgesOnPathEvenThoughtThereShouldBeMaxOne(
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationDefinition,
      String multipleEdgesPath
  ) {
    return new CannotModifyAggregateState(
        String.format(
            "there are multiple edge on path event thought ther should be max one by schema.%n" +
                "Operation name: '%s'%nModification kind: '%s'%nMultiple edges path: '%s'%nModification path: '%s'",
            operationDefinition.getDefinitionType(),
            modificationDefinition.getKind(),
            multipleEdgesPath,
            modificationDefinition.getModificationPath()
        )
    );
  }

  public static CannotModifyAggregateState becauseThereAreIsNodeEdgeOnPathEvenThoughtThereShouldBeOne(
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationDefinition,
      String multipleEdgesPath
  ) {
    return new CannotModifyAggregateState(
        String.format(
            "the node you are trying to modify should already exist. Please add it first with appropriate command.%n"
                +
                "Operation name: '%s'%nMissing node path: '%s'%nModification kind: '%s'%nModification path: '%s'",
            operationDefinition.getDefinitionType(),
            multipleEdgesPath,
            modificationDefinition.getKind(),
            modificationDefinition.getModificationPath()
        )
    );
  }
}
