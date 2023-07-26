package ai.stapi.graphsystem.aggregategraphstatemodifier;

import ai.stapi.graph.exceptions.NodeNotFound;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.graphsystem.aggregatedefinition.model.EventFactoryModification;
import ai.stapi.graphsystem.aggregategraphstatemodifier.exceptions.CannotModifyAggregateState;
import ai.stapi.graphsystem.messaging.command.DynamicCommand;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.schema.structureSchema.ComplexStructureType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EventFactoryModificationTraverser {

    public TraversableNode getTraversingStartNode(
        String aggregateType,
        UniqueIdentifier aggregateId,
        Map<String, Object> possibleStartIds,
        EventFactoryModification modificationDefinition,
        ComplexStructureType operationStructureType,
        InMemoryGraphRepository aggregateRepo
    ) {
        var startIdParameterName = modificationDefinition.getStartIdParameterName();
        if (startIdParameterName == null) {
            if (aggregateRepo.nodeExists(aggregateId, aggregateType)) {
                return aggregateRepo.loadNode(aggregateId, aggregateType);
            } else {
                return new TraversableNode(aggregateId, aggregateType);
            }
        } else {
            var startIdValue = possibleStartIds.get(startIdParameterName);
            if (!(startIdValue instanceof String stringStartId)) {
                throw CannotModifyAggregateState.becauseThereIsNoIdInCommandAtStartIdParameterName(
                    startIdParameterName,
                    modificationDefinition,
                    operationStructureType
                );
            }
            if (!stringStartId.contains("/")) {
                throw CannotModifyAggregateState.becauseStartIdIsNotOfCorrectFormat(
                    stringStartId,
                    startIdParameterName,
                    modificationDefinition,
                    operationStructureType
                );
            }
            var splitId = stringStartId.split("/");
            if (splitId.length != 2) {
                throw CannotModifyAggregateState.becauseStartIdIsNotOfCorrectFormat(
                    stringStartId,
                    startIdParameterName,
                    modificationDefinition,
                    operationStructureType
                );
            }
            try {
                return aggregateRepo.loadNode(
                    new UniqueIdentifier(splitId[1]),
                    splitId[0]
                );
            } catch (NodeNotFound nodeNotFound) {
                throw CannotModifyAggregateState.becauseThereIsNoNodeWithIdSpecifiedAtStartIdParameterName(
                    modificationDefinition,
                    operationStructureType,
                    new UniqueIdentifier(stringStartId),
                    startIdParameterName
                );
            }
        }
    }

    public TraversableNode traverseToModifiedNode(
        TraversableNode currentNode,
        String[] pathToTraverse,
        ComplexStructureType operationDefinition,
        EventFactoryModification modificationDefinition,
        List<String> alreadyTraversedPath
    ) {
        if (pathToTraverse.length < 2) {
            return currentNode;
        }
        var fieldName = pathToTraverse[0];
        var edges = currentNode.getEdges(fieldName);
        var newAlreadyTraversedPath = new ArrayList<>(alreadyTraversedPath);
        newAlreadyTraversedPath.add(fieldName);
        if (edges.size() > 1) {
            throw CannotModifyAggregateState.becauseThereAreEdgesOnPathEvenThoughtThereShouldBeMaxOne(
                modificationDefinition,
                operationDefinition,
                String.join(".", newAlreadyTraversedPath)
            );
        }
        if (edges.isEmpty()) {
            throw CannotModifyAggregateState.becauseThereAreIsNodeEdgeOnPathEvenThoughtThereShouldBeOne(
                modificationDefinition,
                operationDefinition,
                String.join(".", newAlreadyTraversedPath)
            );
        }
        return this.traverseToModifiedNode(
            edges.get(0).getNodeTo(),
            Arrays.copyOfRange(pathToTraverse, 1, pathToTraverse.length),
            operationDefinition,
            modificationDefinition,
            newAlreadyTraversedPath
        );
    }

    public TraversableNode traverseToModifiedNode(
        TraversableNode currentNode,
        String[] pathToTraverse,
        ComplexStructureType operationDefinition,
        EventFactoryModification modificationDefinition
    ) {
        return this.traverseToModifiedNode(
            currentNode,
            pathToTraverse,
            operationDefinition,
            modificationDefinition,
            List.of()
        );
    }
}
