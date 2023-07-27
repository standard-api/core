package ai.stapi.graphsystem.aggregategraphstatemodifier;

import ai.stapi.graph.Graph;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.GraphMappingResult;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.graphoperations.ogmProviders.specific.dynamicObjectGraphMappingProvider.DynamicOgmProvider;
import ai.stapi.graphsystem.aggregatedefinition.model.EventFactoryModification;
import ai.stapi.graphsystem.aggregategraphstatemodifier.exceptions.CannotAddToAggregateState;
import ai.stapi.graphsystem.messaging.command.DynamicCommand;
import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;

import java.util.List;

public class AddAggregateGraphStateModificator extends AbstractAggregateGraphStateModificator {

  public AddAggregateGraphStateModificator(
      GenericObjectGraphMapper objectGraphMapper,
      EventFactoryModificationTraverser eventFactoryModificationTraverser,
      EventModificatorOgmProvider eventModificatorOgmProvider
  ) {
    super(objectGraphMapper, eventFactoryModificationTraverser, eventModificatorOgmProvider);
  }

  @Override
  public GraphMappingResult modify(
      String aggregateType,
      Graph currentAggregateState,
      DynamicCommand command,
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationStructureType,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  ) {
    var inputValueParameterName = modificationDefinition.getInputValueParameterName();
    if (inputValueParameterName == null) {
      throw CannotAddToAggregateState.becauseModificationDefinitionDoesNotHaveInputValueParameterNameSpecified(
          modificationDefinition,
          operationStructureType
      );
    }
    var inputValue = command.getData().get(inputValueParameterName);
    if (inputValue == null) {
      return new GraphMappingResult(
          new Graph(new Node(command.getTargetIdentifier(), aggregateType)),
          List.of()
      );
    }

    var aggregateRepo = currentAggregateState.traversable();
    var traversingStartNode = this.eventFactoryModificationTraverser.getTraversingStartNode(
        aggregateType,
        command.getTargetIdentifier(),
        command.getData(),
        modificationDefinition,
        operationStructureType,
        aggregateRepo
    );


    var modificationPath = modificationDefinition.getModificationPath();
    var splitPath = modificationPath.split("\\.");
    var modifiedNode = this.eventFactoryModificationTraverser.traverseToModifiedNode(
        traversingStartNode,
        splitPath,
        operationStructureType,
        modificationDefinition
    );

    var inputValueSchema = operationStructureType.getField(inputValueParameterName);
    var fieldName = splitPath[splitPath.length - 1];
    if (inputValueSchema.getFloatMax() < 2) {
      if (!modifiedNode.getEdges(fieldName).isEmpty()) {
        throw CannotAddToAggregateState.becauseThereAlreadyIsSuchLeafComplexType(
            modificationDefinition,
            operationStructureType,
            modifiedNode
        );
      }
      if (modifiedNode.hasAttribute(fieldName)) {
        throw CannotAddToAggregateState.becauseThereAlreadyIsSuchLeafAttribute(
            modificationDefinition,
            operationStructureType,
            modifiedNode
        );
      }
    }

    var objectOgm = this.eventModificatorOgmProvider.getOgm(modifiedNode, inputValueSchema, fieldName);
    var fakedObject = this.getMappedObject(inputValue, modifiedNode, fieldName);
    
    return this.objectGraphMapper.mapToGraph(
        objectOgm,
        fakedObject,
        missingFieldResolvingStrategy
    );
  }

  @Override
  public boolean supports(EventFactoryModification modificationDefinition) {
    return modificationDefinition.getKind().equals(EventFactoryModification.ADD);
  }
}
