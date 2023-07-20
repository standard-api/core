package ai.stapi.graphsystem.aggregategraphstatemodifier;

import ai.stapi.graph.Graph;
import ai.stapi.graph.exceptions.NodeNotFound;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.GenericOGMBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.GraphMappingResult;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.graphoperations.ogmProviders.specific.dynamicObjectGraphMappingProvider.DynamicOgmProvider;
import ai.stapi.graphsystem.aggregatedefinition.model.CommandHandlerDefinitionDTO.EventFactory.EventFactoryModification;
import ai.stapi.graphsystem.aggregategraphstatemodifier.exceptions.CannotAddToAggregateState;
import ai.stapi.graphsystem.messaging.command.DynamicCommand;
import ai.stapi.graphsystem.operationdefinition.model.FieldDefinitionWithSource;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.ResourceStructureType;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class AddAggregateGraphStateModificator implements AggregateGraphStateModificator {

  private final StructureSchemaFinder structureSchemaFinder;
  private final DynamicOgmProvider dynamicOgmProvider;
  private final GenericObjectGraphMapper objectGraphMapper;

  public AddAggregateGraphStateModificator(
      StructureSchemaFinder structureSchemaFinder,
      DynamicOgmProvider dynamicOgmProvider,
      GenericObjectGraphMapper objectGraphMapper
  ) {
    this.structureSchemaFinder = structureSchemaFinder;
    this.dynamicOgmProvider = dynamicOgmProvider;
    this.objectGraphMapper = objectGraphMapper;
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
    
    var modificationPath = modificationDefinition.getModificationPath();
    var splitPath = modificationPath.split("\\.");
    var aggregateRepo = currentAggregateState.traversable();
    var startIdParameterName = modificationDefinition.getStartIdParameterName();
    
    TraversableNode traversingStartNode;
    if (startIdParameterName == null) {
      var aggregateId = command.getTargetIdentifier();
      if (aggregateRepo.nodeExists(aggregateId, aggregateType)) {
        traversingStartNode = aggregateRepo.loadNode(aggregateId, aggregateType);
      } else {
        traversingStartNode = new TraversableNode(aggregateId, aggregateType);
      }
    } else {
      var startIdValue = command.getData().get(startIdParameterName);
      if (!(startIdValue instanceof String stringStartId) ) {
        throw CannotAddToAggregateState.becauseThereIsNoIdInCommandAtStartIdParameterName(
            startIdParameterName,
            modificationDefinition,
            operationStructureType
        );
      }
      if (!stringStartId.contains("/")) {
        throw CannotAddToAggregateState.becauseStartIdIsNotOfCorrectFormat(
            stringStartId,
            startIdParameterName,
            modificationDefinition,
            operationStructureType
        );
      }
      var splitId = stringStartId.split("/");
      if (splitId.length != 2) {
        throw CannotAddToAggregateState.becauseStartIdIsNotOfCorrectFormat(
            stringStartId,
            startIdParameterName,
            modificationDefinition,
            operationStructureType
        );
      }
      try {
        traversingStartNode = aggregateRepo.loadNode(
            new UniqueIdentifier(splitId[1]),
            splitId[0]
        );
      } catch (NodeNotFound nodeNotFound) {
        throw CannotAddToAggregateState.becauseThereIsNoNodeWithIdSpecifiedAtStartIdParameterName(
            modificationDefinition,
            operationStructureType,
            new UniqueIdentifier(stringStartId),
            startIdParameterName
        );
      }
    }

    var modifiedNode = this.traverseToModifiedNode(
        traversingStartNode,
        modificationPath.split("\\."),
        List.of(),
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
    
    var objectOgm = new ObjectGraphMappingBuilder().setGraphDescription(
        new GraphDescriptionBuilder().addNodeDescription(modifiedNode.getType())
    );
    objectOgm
        .addField("id")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(new UuidIdentityDescription());

    
    var dataField = objectOgm.addField(fieldName);

    var edge = new GraphDescriptionBuilder().addOutgoingEdge(fieldName);
    if (inputValueSchema.isUnionType()) {
      if (inputValueSchema.isList()) {
        dataField
            .addListAsObjectFieldMapping()
            .addInterfaceChildDefinition()
            .setGraphDescription(edge);
      } else {
        dataField
            .setRelation(edge)
            .addInterfaceAsObjectFieldMapping();
      }
    } else {
      var type = inputValueSchema.getTypes().get(0).getType();
      var inputValueType = this.structureSchemaFinder.getStructureType(type);
      if (inputValueType instanceof ComplexStructureType) {
        var inputOgm = this.dynamicOgmProvider.provideGraphMapping(type);
        var inputOgmBuilder = new GenericOGMBuilder()
            .copyGraphMappingAsBuilder(inputOgm);
        if (inputValueSchema.isList()) {
          dataField
              .addListAsObjectFieldMapping()
              .setGraphDescription(edge)
              .setChildDefinition(inputOgmBuilder);
        } else {
          dataField
              .setRelation(edge)
              .setOgmBuilder(inputOgmBuilder);
        }
      } else {
        var primitiveOgm = this.dynamicOgmProvider.provideOgmForPrimitive(
            type,
            new FieldDefinition(
                fieldName,
                inputValueSchema.getMin(),
                inputValueSchema.getMax(),
                inputValueSchema.getDescription(),
                inputValueSchema.getTypes(),
                inputValueSchema.getParentDefinitionType()
            )
        );
        var primitiveOgmBuilder = new GenericOGMBuilder().copyGraphMappingAsBuilder(primitiveOgm);
        dataField.setOgmBuilder(primitiveOgmBuilder);
      }
    }

    var fakedObject = new HashMap<>(Map.of(
        "id", modifiedNode.getId().getId(),
        fieldName, inputValue
    ));
    return this.objectGraphMapper.mapToGraph(
        objectOgm.build(),
        fakedObject,
        missingFieldResolvingStrategy
    );
  }

  @Override
  public boolean supports(EventFactoryModification modificationDefinition) {
    return modificationDefinition.getKind().equals(EventFactoryModification.ADD);
  }

  private TraversableNode traverseToModifiedNode(
      TraversableNode currentNode,
      String[] pathToTraverse,
      List<String> alreadyTraversedPath,
      ComplexStructureType operationDefinition,
      EventFactoryModification modificationDefinition
  ) {
    if (pathToTraverse.length < 2) {
      return currentNode;
    }
    var fieldName = pathToTraverse[0];
    var edges = currentNode.getEdges(fieldName);
    var newAlreadyTraversedPath = new ArrayList<>(alreadyTraversedPath);
    newAlreadyTraversedPath.add(fieldName);
    if (edges.size() > 1) {
      throw CannotAddToAggregateState.becauseThereAreEdgesOnPathEvenThoughtThereShouldBeMaxOne(
          modificationDefinition,
          operationDefinition,
          String.join(".", newAlreadyTraversedPath)
      );
    }
    if (edges.isEmpty()) {
      throw CannotAddToAggregateState.becauseThereAreIsNodeEdgeOnPathEvenThoughtThereShouldBeOne(
          modificationDefinition,
          operationDefinition,
          String.join(".", newAlreadyTraversedPath)
      );
    }
    return this.traverseToModifiedNode(
        edges.get(0).getNodeTo(),
        Arrays.copyOfRange(pathToTraverse, 1, pathToTraverse.length),
        newAlreadyTraversedPath,
        operationDefinition,
        modificationDefinition
    );
  }
}
