package ai.stapi.graphsystem.aggregategraphstatemodifier;

import ai.stapi.graph.Graph;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.GraphMappingResult;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.graphoperations.ogmProviders.specific.dynamicObjectGraphMappingProvider.DynamicOgmProvider;
import ai.stapi.graphsystem.aggregatedefinition.model.EventFactoryModification;
import ai.stapi.graphsystem.aggregategraphstatemodifier.exceptions.CannotUpsertToAggregateState;
import ai.stapi.graphsystem.messaging.command.DynamicCommand;
import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;

import java.util.List;

public class UpsertAggregateGraphStateModificator extends AbstractAggregateGraphStateModificator {

  public UpsertAggregateGraphStateModificator(
      StructureSchemaFinder structureSchemaFinder,
      DynamicOgmProvider dynamicOgmProvider,
      GenericObjectGraphMapper objectGraphMapper,
      EventFactoryModificationTraverser eventFactoryModificationTraverser
  ) {
    super(structureSchemaFinder, dynamicOgmProvider, objectGraphMapper, eventFactoryModificationTraverser);
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
    var mainNodeId = command.getTargetIdentifier();
    var inputValueParameterName = modificationDefinition.getInputValueParameterName();
    if (inputValueParameterName == null) {
      throw CannotUpsertToAggregateState.becauseModificationDefinitionDoesNotHaveInputValueParameterNameSpecified(
          modificationDefinition,
          operationStructureType,
          mainNodeId
      );
    }
    var inputValue = command.getData().get(inputValueParameterName);
    if (inputValue == null) {
      return new GraphMappingResult(
          new Graph(),
          List.of()
      );
    }
    
    var aggregateRepo = currentAggregateState.traversable();
    if (!aggregateRepo.nodeExists(mainNodeId, aggregateType)) {
      throw CannotUpsertToAggregateState.becauseMainAggregateNodeDoesntExist(
          operationStructureType,
          modificationDefinition,
          mainNodeId
      );
    }
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
    var objectOgm = this.getOgm(modifiedNode, inputValueSchema, fieldName);
    var fakedObject = this.getMappedObject(inputValue, modifiedNode, fieldName);

    return this.objectGraphMapper.mapToGraph(
        objectOgm,
        fakedObject,
        missingFieldResolvingStrategy
    );
  }

  @Override
  public boolean supports(EventFactoryModification modificationDefinition) {
    return modificationDefinition.getKind().equals(EventFactoryModification.UPSERT);
  }
}
