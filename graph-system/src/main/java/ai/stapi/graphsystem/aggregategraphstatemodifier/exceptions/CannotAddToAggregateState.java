package ai.stapi.graphsystem.aggregategraphstatemodifier.exceptions;

import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.graphsystem.aggregatedefinition.model.CommandHandlerDefinitionDTO;
import ai.stapi.graphsystem.aggregatedefinition.model.EventFactoryModification;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.schema.structureSchema.ComplexStructureType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CannotAddToAggregateState extends RuntimeException {

  private CannotAddToAggregateState(String becauseMessage) {
    super("Cannot add to aggregate state, because " + becauseMessage);
  }

  public CannotAddToAggregateState(List<CannotAddToAggregateState> cannotAddToAggregateStateList) {
    super(
        cannotAddToAggregateStateList.stream()
            .map(Throwable::getMessage)
            .collect(Collectors.joining("\n"))
    );
  }

  public static CannotAddToAggregateState becauseModificationPathIsInvalid(
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationDefinition
  ) {
    return new CannotAddToAggregateState(
        String.format(
            "modification path present in modification definition was invalid.%n" +
                "Operation name: '%s'%nModification kind: '%s'%nModification path: '%s'",
            operationDefinition.getDefinitionType(),
            modificationDefinition.getKind(),
            modificationDefinition.getModificationPath()
        )
    );
  }

  public static CannotAddToAggregateState becauseModificationPathDoesNotStartWithCorrectAggregateType(
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationDefinition,
      String actualAggregateType
  ) {
    return new CannotAddToAggregateState(
        String.format(
            "modification path does not start with correct aggregate type.%n" +
                "Operation name: '%s'%nModification kind: '%s'%nModification path: '%s'%n" +
                "Actual aggregate type: '%s'",
            operationDefinition.getDefinitionType(),
            modificationDefinition.getKind(),
            modificationDefinition.getModificationPath(),
            actualAggregateType
        )
    );
  }

  public static CannotAddToAggregateState becauseModificationPathContainsFieldNameNotPresentInSchema(
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationDefinition
  ) {
    return new CannotAddToAggregateState(
        String.format(
            "modification path present in modification definition "
                + "contains field name not present in schema.%n" +
                "Operation name: '%s'%nModification kind: '%s'%nModification path: '%s'",
            operationDefinition.getDefinitionType(),
            modificationDefinition.getKind(),
            modificationDefinition.getModificationPath()
        )
    );
  }

  public static CannotAddToAggregateState becauseModificationPathContainsFieldNameWhichIsPrimitiveButItShouldNot(
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationDefinition
  ) {
    return new CannotAddToAggregateState(
        String.format(
            "modification path present in modification definition "
                + "contains field name which is of primitive type, but it is not end of path.%n" +
                "Operation name: '%s'%nModification kind: '%s'%nModification path: '%s'",
            operationDefinition.getDefinitionType(),
            modificationDefinition.getKind(),
            modificationDefinition.getModificationPath()
        )
    );
  }

  public static CannotAddToAggregateState becauseModificationDefinitionDoesNotHaveInputValueParameterNameSpecified(
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationDefinition
  ) {
    return new CannotAddToAggregateState(
        String.format(
            "modification definition does not have input value parameter name specified.%n" +
                "Operation name: '%s'%nModification kind: '%s'%nModification path: '%s'",
            operationDefinition.getDefinitionType(),
            modificationDefinition.getKind(),
            modificationDefinition.getModificationPath()
        )
    );
  }

  public static CannotAddToAggregateState becauseModifiedNodeSpecifiedByModificationPathWasNotPresent(
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationDefinition
  ) {
    var modificationPath = modificationDefinition.getModificationPath();
    var split = modificationPath.split("\\.");
    return new CannotAddToAggregateState(
        String.format(
            "node to be modified (specified by modification path) was not present in aggregate state.%n"
                +
                "Operation name: '%s'%nModification kind: '%s'%nExpected present parent node path: %s%nWhole Modification path: '%s'",
            operationDefinition.getDefinitionType(),
            modificationDefinition.getKind(),
            String.join(".", Arrays.copyOfRange(split, 0, split.length - 1)),
            modificationPath
        )
    );
  }

  public static CannotAddToAggregateState becauseThereAreIndistinguishableNodes(
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationDefinition,
      String expectedIdSourcePath
  ) {
    return new CannotAddToAggregateState(
        String.format(
            "modification definition has path which leads through list. %s%n%s%n" +
                "Operation name: '%s'%nModification kind: '%s'%nModification path: '%s'",
            "But there is no id to distinguish between the items.",
            String.format(
                "Maybe invalid command handler definition? You can fix this by adding parameter of type id to operation definition with source path: '%s'.",
                expectedIdSourcePath
            ),
            operationDefinition.getDefinitionType(),
            modificationDefinition.getKind(),
            modificationDefinition.getModificationPath()
        )
    );
  }

  public static CannotAddToAggregateState becauseThereAreMoreWaysToDistinguisNodes(
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationStructureType,
      String sourcePathOfId
  ) {
    return new CannotAddToAggregateState(
        String.format(
            "modification definition has path which leads through multiple nodes. %s%n" +
                "Operation name: '%s'%nModification kind: '%s'%nModification path: '%s'",
            String.format(
                "But there are multiple parameters on operation definition with source path: '%s'.",
                sourcePathOfId),
            operationStructureType.getDefinitionType(),
            modificationDefinition.getKind(),
            modificationDefinition.getModificationPath()
        )
    );
  }

  public static CannotAddToAggregateState becauseThereIsNoNodeWithIdSpecifiedAtSourcePath(
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationStructureType,
      String sourcePathOfId,
      UniqueIdentifier id
  ) {
    return new CannotAddToAggregateState(
        String.format(
            "there is no node with id specified at source path.%n" +
                "Operation name: '%s'%nNode path: '%s'%nId: '%s'%nModification kind: '%s'%n"
                + "Modification path: '%s'",
            operationStructureType.getDefinitionType(),
            sourcePathOfId,
            id.getId(),
            modificationDefinition.getKind(),
            modificationDefinition.getModificationPath()
        )
    );
  }

  public static CannotAddToAggregateState becauseThereIsNoSourcePathAtStartIdParameterName(
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationStructureType,
      UniqueIdentifier id,
      String startIdParameterName
  ) {
    return new CannotAddToAggregateState(
        String.format(
            "there is no node with source path specified at start id parameter name.%n" +
                "Operation name: '%s'%nId: '%s'%nModification kind: '%s'%n"
                + "Start id parameter name: '%s'",
            operationStructureType.getDefinitionType(),
            id.getId(),
            modificationDefinition.getKind(),
            startIdParameterName
        )
    );
  }

  public static CannotAddToAggregateState becauseThereAlreadyIsSuchLeafComplexType(
      EventFactoryModification modificationDefinition, 
      ComplexStructureType operationStructureType,
      TraversableNode modifiedNode
  ) {
    return new CannotAddToAggregateState(
        String.format(
            "there already is such singular complex type, but add operation cannot modify existing data. " +
                "Use command with upsert modification instead.%n" +
                "Operation name: '%s'%nModification kind: '%s'%nModification path: '%s'%nModified node identifier: '%s/%s'",
            operationStructureType.getDefinitionType(),
            modificationDefinition.getKind(),
            modificationDefinition.getModificationPath(),
            modifiedNode.getType(),
            modifiedNode.getId().getId()
        )
    );
  }

  public static CannotAddToAggregateState becauseThereAlreadyIsSuchLeafAttribute(
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationStructureType,
      TraversableNode modifiedNode
  ) {
    return new CannotAddToAggregateState(
        String.format(
            "there already is such leaf attribute, but add operation cannot modify existing data. " +
                "Use command with upsert modification instead.%n" +
                "Operation name: '%s'%nModification kind: '%s'%nModification path: '%s'%nModified node identifier: '%s/%s'",
            operationStructureType.getDefinitionType(),
            modificationDefinition.getKind(),
            modificationDefinition.getModificationPath(),
            modifiedNode.getType(),
            modifiedNode.getId().getId()
        )
    );
  }
}
